package com.labs.lg.food.ordering.system.order.service.domain.ports.output.message.publisher.payment;


import com.labs.lg.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.labs.lg.pentagon.common.domain.event.publisher.DomainEventPublisher;

public interface OrderCancelledPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCancelledEvent> {
}
