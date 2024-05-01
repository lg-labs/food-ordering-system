package com.labs.lg.food.ordering.system.restaurant.service.domain.ports.output.message.publisher;

import com.labs.lg.food.ordering.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import com.lg5.spring.outbox.OutboxStatus;

import java.util.function.BiConsumer;

public interface RestaurantApprovalResponseMessagePublisher {

    void publish(OrderOutboxMessage orderOutboxMessage,
                 BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback);
}
