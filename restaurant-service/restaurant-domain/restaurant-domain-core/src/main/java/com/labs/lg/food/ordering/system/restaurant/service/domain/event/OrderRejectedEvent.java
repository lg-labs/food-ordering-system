package com.labs.lg.food.ordering.system.restaurant.service.domain.event;

import com.labs.lg.food.ordering.system.domain.valueobject.RestaurantId;
import com.labs.lg.food.ordering.system.restaurant.service.domain.entity.OrderApproval;
import com.labs.lg.pentagon.common.domain.event.publisher.DomainEventPublisher;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderRejectedEvent extends OrderApprovalEvent {
  private final DomainEventPublisher<OrderRejectedEvent> orderRejectedEventDomainEventPublisher;

  public OrderRejectedEvent(OrderApproval orderApproval,
                            RestaurantId restaurantId,
                            List<String> failureMessages,
                            ZonedDateTime createdAt,
                            DomainEventPublisher<OrderRejectedEvent> orderRejectedEventDomainEventPublisher) {
    super(orderApproval, restaurantId, failureMessages, createdAt);
    this.orderRejectedEventDomainEventPublisher = orderRejectedEventDomainEventPublisher;
  }

  @Override
  public void fire() {
    orderRejectedEventDomainEventPublisher.publish(this);
  }
}
