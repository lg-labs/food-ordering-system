package com.labs.lg.food.ordering.system.payment.service.message.listener.kafka;

import com.labs.lg.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.labs.lg.food.ordering.system.kafka.order.avro.model.PaymentOrderStatus;
import com.labs.lg.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.labs.lg.food.ordering.system.payment.service.domain.exception.PaymentApplicationServiceException;
import com.labs.lg.food.ordering.system.payment.service.domain.exception.PaymentNotFoundException;
import com.labs.lg.food.ordering.system.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import com.labs.lg.food.ordering.system.payment.service.message.mapper.PaymentMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLState;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class PaymentRequestKafkaMessageListener implements KafkaConsumer<PaymentRequestAvroModel> {
    private final PaymentRequestMessageListener paymentRequestMessageListener;
    private final PaymentMessagingDataMapper paymentMessagingDataMapper;

    public PaymentRequestKafkaMessageListener(PaymentRequestMessageListener paymentRequestMessageListener, PaymentMessagingDataMapper paymentMessagingDataMapper) {
        this.paymentRequestMessageListener = paymentRequestMessageListener;
        this.paymentMessagingDataMapper = paymentMessagingDataMapper;
    }

    @Override
    @KafkaListener(
            id = "${kafka-consumer-config.payment-consumer-group-id}",
            topics = "${payment-service.payment-request-topic-name}"
    )
    public void receive(@Payload List<PaymentRequestAvroModel> message,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number payment requests received with keys:{}, partitions:{}, and offsets: {} ",
                message.size(),
                key,
                partitions.toString(),
                offsets);

        message.forEach(paymentRequestAvroModel -> {

            try {
                if (PaymentOrderStatus.PENDING == paymentRequestAvroModel.getPaymentOrderStatus()) {
                    log.info("Processing payment for order id: {}", paymentRequestAvroModel.getOrderId());
                    paymentRequestMessageListener.completePayment(paymentMessagingDataMapper
                            .paymentRequestAvroModelToPaymentRequest(paymentRequestAvroModel));
                } else if (PaymentOrderStatus.CANCELLED == paymentRequestAvroModel.getPaymentOrderStatus()) {
                    log.info("Cancelling payment for order id: {}", paymentRequestAvroModel.getOrderId());
                    paymentRequestMessageListener.cancelPayment(paymentMessagingDataMapper
                            .paymentRequestAvroModelToPaymentRequest(paymentRequestAvroModel));
                }
            } catch (DataAccessException e) {
                SQLException sqlException = (SQLException) e.getRootCause();
                if (sqlException != null && sqlException.getSQLState() != null &&
                        PSQLState.UNIQUE_VIOLATION.getState().equals(sqlException.getSQLState())) {
                    //NO-OP for unique constraint exception
                    log.error("Caught unique constraint exception with sql state: {} " +
                                    "in PaymentRequestKafkaListener for order id: {}",
                            sqlException.getSQLState(), paymentRequestAvroModel.getOrderId());
                } else {
                    throw new PaymentApplicationServiceException("Throwing DataAccessException in" +
                            " PaymentRequestKafkaListener: " + e.getMessage(), e);
                }
            } catch (PaymentNotFoundException e) {
                //NO-OP for PaymentNotFoundException
                log.error("No payment found for order id: {}", paymentRequestAvroModel.getOrderId());
            }
        });
    }
}
