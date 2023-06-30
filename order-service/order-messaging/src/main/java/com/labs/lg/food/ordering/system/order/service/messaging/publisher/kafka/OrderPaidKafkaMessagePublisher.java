package com.labs.lg.food.ordering.system.order.service.messaging.publisher.kafka;

import com.labs.lg.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.labs.lg.food.ordering.system.kafka.producer.KafkaMessageHelper;
import com.labs.lg.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.labs.lg.food.ordering.system.order.service.domain.config.OrderServiceConfigData;
import com.labs.lg.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import com.labs.lg.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class OrderPaidKafkaMessagePublisher implements OrderPaidRestaurantRequestMessagePublisher {

    private final OrderServiceConfigData orderServiceConfigData;
    private final OrderMessagingDataMapper mapper;
    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> producer;
    private final KafkaMessageHelper kafkaMessageHelper;

    @Override
    public void publish(OrderPaidEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();
        log.info("Received OrderPaidEvent for order id: {}", orderId);
        try {

            RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel = mapper
                    .orderPaidEventToRestaurantApprovalRequestAvroModel(domainEvent);

            producer.send(orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                orderId,
                    restaurantApprovalRequestAvroModel,
                kafkaMessageHelper.getKafkaCallback(orderServiceConfigData.getPaymentRequestTopicName(),
                        restaurantApprovalRequestAvroModel,
                        orderId,
                        "restaurantApprovalRequestAvroModel"
                ));
        log.info("PaymentRequestAvroModel sent to kafka for order id {}", orderId);
        }catch (Exception e){
            log.error("Error while sending PaymentRequestAvroModel message"
                    + "to kafka with order id: {}, error: {}", orderId, e.getMessage()
            );
        }
    }
}
