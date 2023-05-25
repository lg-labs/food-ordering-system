package com.labs.lg.food.ordering.system.order.service.messaging.listener.kafka;

import com.labs.lg.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.labs.lg.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.labs.lg.food.ordering.system.kafka.order.avro.model.PaymentStatus;
import com.labs.lg.food.ordering.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import com.labs.lg.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {
    private final PaymentResponseMessageListener paymentResponseMessageListener;
    private final OrderMessagingDataMapper mapper;

    public PaymentResponseKafkaListener(PaymentResponseMessageListener paymentResponseMessageListener, OrderMessagingDataMapper mapper) {
        this.paymentResponseMessageListener = paymentResponseMessageListener;
        this.mapper = mapper;
    }

    @Override
    @KafkaListener(
            id = "${kafka-consumer-config.payment-consumer-group-id}",
            topics = "${order-service.payment-response-topic-name}"
    )
    public void receive(@Payload  List<PaymentResponseAvroModel> message,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number payment responses received with keys:{}, partitions:{}, and offsets: {} ",
                message.size(),
                key,
                partitions.toString(),
                offsets);

        message.forEach(paymentResponseAvroModel -> {

            if (PaymentStatus.COMPLETED == paymentResponseAvroModel.getPaymentStatus()){
                log.info("Processing successful payment for order id: {}", paymentResponseAvroModel.getOrderId());
                paymentResponseMessageListener.paymentCompleted(mapper.paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel));
            }else if (PaymentStatus.CANCELLED == paymentResponseAvroModel.getPaymentStatus() ||
                    PaymentStatus.FAILED == paymentResponseAvroModel.getPaymentStatus()){
                log.info("Processing unsuccessful payment for order id: {}", paymentResponseAvroModel.getOrderId());
                paymentResponseMessageListener.paymentCancelled(mapper.paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel));

            }

        });

    }
}
