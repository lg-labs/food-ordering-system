package com.labs.lg.food.ordering.system.order.service.domain;

import com.labs.lg.food.ordering.system.domain.valueobject.OrderId;
import com.labs.lg.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Order;
import com.labs.lg.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.labs.lg.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.labs.lg.food.ordering.system.saga.SagaStep;
import com.labs.lg.pentagon.common.domain.event.EmptyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderPaymentSaga implements SagaStep<PaymentResponse, OrderPaidEvent, EmptyEvent> {
  private final OrderDomainService orderDomainService;
  private final OrderSagaHelper orderSagaHelper;
  private final OrderPaidRestaurantRequestMessagePublisher orderPaidRestaurantRequestMessagePublisher;

  public OrderPaymentSaga(OrderDomainService orderDomainService,
                          OrderSagaHelper orderSagaHelper,
                          OrderPaidRestaurantRequestMessagePublisher orderPaidRestaurantRequestMessagePublisher) {
    this.orderDomainService = orderDomainService;
    this.orderSagaHelper = orderSagaHelper;
    this.orderPaidRestaurantRequestMessagePublisher = orderPaidRestaurantRequestMessagePublisher;
  }

  @Override
  @Transactional
  public OrderPaidEvent process(PaymentResponse paymentResponse) {
    final String orderId = paymentResponse.getOrderId();
    log.info("Completing payment for order id: {}", orderId);
    Order order = orderSagaHelper.findOrder(orderId);
    OrderPaidEvent domainEvent = orderDomainService.payOrder(order, orderPaidRestaurantRequestMessagePublisher);
    orderSagaHelper.saveOrder(order);
    log.info("Order with id:{} is paid", order.getId().getValue());
    return domainEvent;
  }


  @Override
  @Transactional
  public EmptyEvent rollback(PaymentResponse paymentResponse) {
    final String orderId = paymentResponse.getOrderId();
    log.info("Cancelling payment for order id: {}", orderId);
    Order order = orderSagaHelper.findOrder(orderId);
    orderDomainService.cancelOrder(order, paymentResponse.getFailureMessages());
    log.info("Order with id:{} is cancelled", order.getId().getValue());
    return EmptyEvent.INSTANCE;
  }


}
