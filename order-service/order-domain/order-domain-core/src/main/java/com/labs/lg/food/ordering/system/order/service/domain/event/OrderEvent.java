package com.labs.lg.food.ordering.system.order.service.domain.event;

import com.labs.lg.food.ordering.system.order.service.domain.entity.Order;
import com.labs.lg.pentagon.common.domain.event.DomainEvent;

import java.time.ZonedDateTime;

public abstract class OrderEvent implements DomainEvent<Order> {

    private final Order order;
    private final ZonedDateTime createdAt;

    protected OrderEvent(Order order, ZonedDateTime createdAt) {
        this.order = order;
        this.createdAt = createdAt;
    }

    public Order getOrder() {
        return order;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
