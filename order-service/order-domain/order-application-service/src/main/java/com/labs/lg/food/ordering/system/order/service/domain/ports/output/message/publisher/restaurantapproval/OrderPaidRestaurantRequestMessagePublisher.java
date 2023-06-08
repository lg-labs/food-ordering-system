package com.labs.lg.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval;

import com.labs.lg.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.labs.lg.pentagon.common.domain.event.publisher.DomainEventPublisher;

public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
}
