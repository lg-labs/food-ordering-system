package com.labs.lg.food.ordering.system.order.service.messaging.publisher.kafka;

import com.labs.lg.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.labs.lg.food.ordering.system.kafka.producer.KafkaMessageHelper;
import com.labs.lg.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.labs.lg.food.ordering.system.order.service.domain.config.OrderServiceConfigData;
import com.labs.lg.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import com.labs.lg.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreateOrderKafkaMessagePublisher implements OrderCreatedPaymentRequestMessagePublisher {

    private final OrderServiceConfigData orderServiceConfigData;
    private final OrderMessagingDataMapper mapper;
    private final KafkaProducer<String, PaymentRequestAvroModel> producer;
    private final KafkaMessageHelper kafkaMessageHelper;

    public CreateOrderKafkaMessagePublisher(OrderServiceConfigData orderServiceConfigData, OrderMessagingDataMapper mapper,
                                            KafkaProducer<String, PaymentRequestAvroModel> producer,
                                            KafkaMessageHelper kafkaMessageHelper) {
        this.orderServiceConfigData = orderServiceConfigData;
        this.mapper = mapper;
        this.producer = producer;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(OrderCreatedEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();
        log.info("Received OrderCreatedEvent for order id: {}", orderId);
        try {


        PaymentRequestAvroModel paymentRequestAvroModel = mapper.orderCreatedEventToPaymentRequestAvroModel(domainEvent);

        producer.send(orderServiceConfigData.getPaymentRequestTopicName(),
                orderId,
                paymentRequestAvroModel,
                kafkaMessageHelper.getKafkaCallback(
                        orderServiceConfigData.getPaymentRequestTopicName(),
                        paymentRequestAvroModel,
                        orderId,
                        "PaymentRequestAvroModel"
                ));
        log.info("PaymentRequestAvroModel sent to kafka for order id {}", orderId);
        }catch (Exception e){
            log.error("Error while sending PaymentRequestAvroModel message"
                    + "to kafka with order id: {}, error: {}", orderId, e.getMessage()
            );
        }
    }
}
