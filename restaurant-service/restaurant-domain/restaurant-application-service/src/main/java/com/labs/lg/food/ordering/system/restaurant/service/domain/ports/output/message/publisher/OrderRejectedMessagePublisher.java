package com.labs.lg.food.ordering.system.restaurant.service.domain.ports.output.message.publisher;

import com.labs.lg.food.ordering.system.restaurant.service.domain.event.OrderRejectedEvent;
import com.labs.lg.pentagon.common.domain.event.publisher.DomainEventPublisher;

public interface OrderRejectedMessagePublisher extends DomainEventPublisher<OrderRejectedEvent> {
}
