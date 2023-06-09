package com.labs.lg.food.ordering.system.restaurant.service.domain.event;

import com.labs.lg.food.ordering.system.domain.valueobject.RestaurantId;
import com.labs.lg.food.ordering.system.restaurant.service.domain.entity.OrderApproval;
import com.labs.lg.pentagon.common.domain.event.publisher.DomainEventPublisher;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderApprovedEvent extends OrderApprovalEvent {
  private final DomainEventPublisher<OrderApprovedEvent> orderApprovedEventDomainEventPublisher;

  public OrderApprovedEvent(OrderApproval orderApproval,
                            RestaurantId restaurantId,
                            List<String> failureMessages,
                            ZonedDateTime createdAt,
                            DomainEventPublisher<OrderApprovedEvent> orderApprovedEventDomainEventPublisher) {
    super(orderApproval, restaurantId, failureMessages, createdAt);
    this.orderApprovedEventDomainEventPublisher = orderApprovedEventDomainEventPublisher;
  }

  @Override
  public void fire() {
    orderApprovedEventDomainEventPublisher.publish(this);
  }
}
