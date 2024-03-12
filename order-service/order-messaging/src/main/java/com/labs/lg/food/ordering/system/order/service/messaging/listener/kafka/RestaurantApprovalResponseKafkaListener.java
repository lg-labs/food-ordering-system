package com.labs.lg.food.ordering.system.order.service.messaging.listener.kafka;

import com.labs.lg.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.labs.lg.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus;
import com.labs.lg.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.labs.lg.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import com.labs.lg.food.ordering.system.order.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import com.labs.lg.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.labs.lg.food.ordering.system.order.service.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;

@Slf4j
@Component
public class RestaurantApprovalResponseKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {
    private final RestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener;
    private final OrderMessagingDataMapper mapper;

    public RestaurantApprovalResponseKafkaListener(RestaurantApprovalResponseMessageListener restaurantApprovalResponseMessageListener, OrderMessagingDataMapper mapper) {
        this.restaurantApprovalResponseMessageListener = restaurantApprovalResponseMessageListener;
        this.mapper = mapper;
    }

    @Override
    @KafkaListener(
            id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${order-service.restaurant-approval-response-topic-name}"
    )
    public void receive(@Payload List<RestaurantApprovalResponseAvroModel> message,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number restaurant approval responses received with keys:{}, partitions:{}, and offsets: {} ",
                message.size(),
                key,
                partitions.toString(),
                offsets);
        message.forEach(restaurantApprovalResponseAvroModel -> {

            try {
                if (OrderApprovalStatus.APPROVED == restaurantApprovalResponseAvroModel.getOrderApprovalStatus()) {
                    log.info("Processing approved order to order id: {}", restaurantApprovalResponseAvroModel.getOrderId());
                    restaurantApprovalResponseMessageListener.orderApproved(mapper
                            .restaurantApprovalResponseAvroModelToRestaurantApprovalResponse(restaurantApprovalResponseAvroModel));
                } else if (OrderApprovalStatus.REJECTED == restaurantApprovalResponseAvroModel.getOrderApprovalStatus()) {
                    log.info("Processing rejected order to order id: {}, with message failed: {}",
                            restaurantApprovalResponseAvroModel.getOrderId(),
                            String.join(FAILURE_MESSAGE_DELIMITER,
                                    restaurantApprovalResponseAvroModel.getFailureMessages()));
                    restaurantApprovalResponseMessageListener.orderRejected(mapper
                            .restaurantApprovalResponseAvroModelToRestaurantApprovalResponse(restaurantApprovalResponseAvroModel));

                }
            } catch (OptimisticLockingFailureException e) {
                //NO-OP for optimistic lock. This means another thread finished the work, do not throw error to prevent reading the data from kafka again!
                log.error("Caught optimistic locking exception in RestaurantApprovalResponseKafkaListener for order id: {}",
                        restaurantApprovalResponseAvroModel.getOrderId());
            } catch (OrderNotFoundException e) {
                //NO-OP for OrderNotFoundException
                log.error("No order found for order id: {}", restaurantApprovalResponseAvroModel.getOrderId());
            }

        });

    }
}
