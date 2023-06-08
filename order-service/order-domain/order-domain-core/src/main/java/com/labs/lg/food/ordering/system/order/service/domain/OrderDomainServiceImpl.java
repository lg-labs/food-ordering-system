package com.labs.lg.food.ordering.system.order.service.domain;

import com.labs.lg.food.ordering.system.order.service.domain.entity.Order;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Product;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Restaurant;
import com.labs.lg.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.labs.lg.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.labs.lg.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.labs.lg.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.labs.lg.pentagon.common.domain.event.publisher.DomainEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.labs.lg.pentagon.common.domain.DomainConstants.UTC;


/**
 * <h1> Bounded Context</h1>
 * <h2>Order Service</h2>
 */

public class OrderDomainServiceImpl implements OrderDomainService {
  private static final Logger log = LoggerFactory.getLogger(OrderDomainServiceImpl.class);


  @Override
  public OrderCreatedEvent validateAndInitiateOrder(Order order,
                                                    Restaurant restaurant,
                                                    DomainEventPublisher<OrderCreatedEvent>
                                                          orderCreatedEventDomainEventPublisher) {
    validateRestaurant(restaurant);
    setOrderProductInformation(order, restaurant);
    order.validateOrder();
    order.initializeOrder();
    log.info("Order with id: {} is initiate", order.getId().getValue());
    return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)), orderCreatedEventDomainEventPublisher);
  }

  /**
   * TODO: This is not implemented, so it will be implemented when add SAGA Pattern
   */
  @Override
  public OrderPaidEvent payOrder(Order order,
                                 DomainEventPublisher<OrderPaidEvent> orderPaidEventDomainEventPublisher) {
    order.pay();
    log.info("Order with id {} is paid", order.getId().getValue());
    return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)), orderPaidEventDomainEventPublisher);
  }

  @Override
  public void approveOrder(Order order) {
    order.approve();
    log.info("Order with id {} is approved", order.getId().getValue());
  }

  /**
   * TODO: This is not implemented, so it will be implemented when add SAGA Pattern
   */
  @Override
  public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages,
                                                DomainEventPublisher<OrderCancelledEvent>
                                                      orderCancelledEventDomainEventPublisher) {
    order.initCancel(failureMessages);
    log.info("Order payment is cancelling for order id: {} ", order.getId().getValue());
    return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)),
        orderCancelledEventDomainEventPublisher);
  }

  @Override
  public void cancelOrder(Order order, List<String> failureMessages) {
    order.cancel(failureMessages);
    log.info("Order with id: {} is cancelled",order.getId().getValue());
  }

  private void validateRestaurant(Restaurant restaurant) {
    if (!restaurant.isActive()) {
      throw new OrderDomainException("Restaurant with Id "
          + restaurant.getId().getValue()
          + "is currently no active"
      );
    }
  }

  private void setOrderProductInformation(Order order, Restaurant restaurant) {
    order.getItems().forEach(orderItem -> restaurant.getProducts().forEach(restaurantProduct -> {
      Product currentProduct = orderItem.getProduct();
      if (currentProduct.equals(restaurantProduct)) {
        currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(),
                restaurantProduct.getPrice());
      }
    }));
  }
}
