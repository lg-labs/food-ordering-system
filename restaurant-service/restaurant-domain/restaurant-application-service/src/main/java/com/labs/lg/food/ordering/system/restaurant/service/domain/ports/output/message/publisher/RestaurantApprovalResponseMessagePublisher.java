package com.labs.lg.food.ordering.system.restaurant.service.domain.ports.output.message.publisher;

import com.labs.lg.food.ordering.system.outbox.OutboxStatus;
import com.labs.lg.food.ordering.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;

import java.util.function.BiConsumer;

public interface RestaurantApprovalResponseMessagePublisher {

    void publish(OrderOutboxMessage orderOutboxMessage,
                 BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback);
}
