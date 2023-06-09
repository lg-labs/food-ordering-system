package com.labs.lg.food.ordering.system.restaurant.service.domain;

import com.labs.lg.food.ordering.system.domain.valueobject.OrderApprovalStatus;
import com.labs.lg.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import com.labs.lg.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent;
import com.labs.lg.food.ordering.system.restaurant.service.domain.event.OrderApprovedEvent;
import com.labs.lg.food.ordering.system.restaurant.service.domain.event.OrderRejectedEvent;
import com.labs.lg.pentagon.common.domain.event.publisher.DomainEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.labs.lg.pentagon.common.domain.DomainConstants.UTC;

/**
 * <h1> Bounded Context</h1>
 * <h2>Restaurant Service</h2>
 */
public class RestaurantDomainServiceImpl implements RestaurantDomainService {
  private final static Logger log = LoggerFactory.getLogger(RestaurantDomainServiceImpl.class);

  @Override
  public OrderApprovalEvent validateOrder(Restaurant restaurant,
                                          List<String> failureMessages,
                                          DomainEventPublisher<OrderApprovedEvent>
                                              orderApprovedEventDomainEventPublisher,
                                          DomainEventPublisher<OrderRejectedEvent>
                                              orderRejectedEventDomainEventPublisher) {
    restaurant.validateOrder(failureMessages);
    log.info("Validating order with id: {}", restaurant.getOrderDetail().getId().getValue());
    if (failureMessages.isEmpty()) {
      log.info("Order is approved for order id: {}", restaurant.getOrderDetail().getId().getValue());
      restaurant.constructOrderApproval(OrderApprovalStatus.APPROVED);
      return new OrderApprovedEvent(restaurant.getOrderApproval(),
          restaurant.getId(),
          failureMessages, ZonedDateTime.now(ZoneId.of(UTC)),
          orderApprovedEventDomainEventPublisher
          );
    }
    log.info("Order is rejected for order id: {}", restaurant.getOrderDetail().getId().getValue());
    restaurant.constructOrderApproval(OrderApprovalStatus.REJECTED);

    return new OrderRejectedEvent(restaurant.getOrderApproval(),
        restaurant.getId(),
        failureMessages, ZonedDateTime.now(ZoneId.of(UTC)),
        orderRejectedEventDomainEventPublisher);
  }
}
