package com.labs.lg.food.ordering.system.restaurant.service.domain;

import com.labs.lg.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import com.labs.lg.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent;
import com.labs.lg.food.ordering.system.restaurant.service.domain.event.OrderApprovedEvent;
import com.labs.lg.food.ordering.system.restaurant.service.domain.event.OrderRejectedEvent;
import com.labs.lg.pentagon.common.domain.event.publisher.DomainEventPublisher;

import java.util.List;

public interface RestaurantDomainService {

  OrderApprovalEvent validateOrder(Restaurant restaurant,
                                   List<String> failureMessages,
                                   DomainEventPublisher<OrderApprovedEvent> orderApprovedEventDomainEventPublisher,
                                   DomainEventPublisher<OrderRejectedEvent> orderRejectedEventDomainEventPublisher);

}
