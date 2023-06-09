package com.labs.lg.food.ordering.system.restaurant.service.domain.ports.output.message.publisher;

import com.labs.lg.food.ordering.system.restaurant.service.domain.event.OrderApprovedEvent;
import com.labs.lg.pentagon.common.domain.event.publisher.DomainEventPublisher;

public interface OrderApprovedMessagePublisher extends DomainEventPublisher<OrderApprovedEvent> {
}
