package com.labs.lg.food.ordering.system.restaurant.service.message.listener.kafka;


import com.labs.lg.food.ordering.system.message.model.avro.RestaurantApprovalRequestAvroModel;
import com.labs.lg.food.ordering.system.restaurant.service.domain.exception.RestaurantApplicationServiceException;
import com.labs.lg.food.ordering.system.restaurant.service.domain.exception.RestaurantNotFoundException;
import com.labs.lg.food.ordering.system.restaurant.service.domain.ports.input.message.listener.RestaurantApprovalRequestMessageListener;
import com.labs.lg.food.ordering.system.restaurant.service.message.mapper.RestaurantMessagingDataMapper;
import com.lg5.spring.kafka.consumer.KafkaConsumer;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.postgresql.util.PSQLState;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class RestaurantApprovalRequestKafkaListener implements KafkaConsumer<RestaurantApprovalRequestAvroModel> {
    private final RestaurantApprovalRequestMessageListener restaurantApprovalRequestMessageListener;
    private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;

    public RestaurantApprovalRequestKafkaListener(RestaurantApprovalRequestMessageListener
                                                          restaurantApprovalRequestMessageListener,
                                                  RestaurantMessagingDataMapper restaurantMessagingDataMapper) {
        this.restaurantApprovalRequestMessageListener = restaurantApprovalRequestMessageListener;
        this.restaurantMessagingDataMapper = restaurantMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${restaurant-service.restaurant-approval-request-topic-name}")
    public void receive(@Payload @Nullable List<RestaurantApprovalRequestAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) @Nullable List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) @Nullable List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) @Nullable List<Long> offsets) {
        log.info("{} number of orders approval requests received with keys {}, partitions {} and offsets {}"
                        + ", sending for restaurant approval",
                Objects.requireNonNull(messages).size(),
                Objects.requireNonNull(keys),
                Objects.requireNonNull(partitions),
                Objects.requireNonNull(offsets));

        messages.forEach(restaurantApprovalRequestAvroModel -> {
            try {
                log.info("Processing order approval for order id: {}", restaurantApprovalRequestAvroModel.getOrderId());
                restaurantApprovalRequestMessageListener.approveOrder(restaurantMessagingDataMapper
                        .restaurantApprovalRequestAvroModelToRestaurantApproval(restaurantApprovalRequestAvroModel));

            } catch (DataAccessException e) {
                final SQLException sqlException = (SQLException) e.getRootCause();

                if (sqlException != null && sqlException.getSQLState() != null
                        && PSQLState.UNIQUE_VIOLATION.getState().equals(sqlException.getSQLState())) {

                    //NO-OP for unique constraint exception
                    log.error("Caught unique constraint exception with sql state: {} "
                                    + "in RestaurantApprovalRequestKafkaListener for order id: {}",
                            sqlException.getSQLState(), restaurantApprovalRequestAvroModel.getOrderId());
                } else {
                    throw new RestaurantApplicationServiceException("Throwing DataAccessException in"
                            + " RestaurantApprovalRequestKafkaListener: " + e.getMessage(), e);
                }
            } catch (RestaurantNotFoundException e) {
                //NO-OP for RestaurantNotFoundException
                log.error("No restaurant found for restaurant id: {}, and order id: {}",
                        restaurantApprovalRequestAvroModel.getRestaurantId(),
                        restaurantApprovalRequestAvroModel.getOrderId());
            }
        });
    }
}
