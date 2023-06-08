package com.labs.lg.food.ordering.system.order.service.domain.ports.output.message.publisher.payment;

import com.labs.lg.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.labs.lg.pentagon.common.domain.event.publisher.DomainEventPublisher;

public interface OrderCreatedPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCreatedEvent> {
}
