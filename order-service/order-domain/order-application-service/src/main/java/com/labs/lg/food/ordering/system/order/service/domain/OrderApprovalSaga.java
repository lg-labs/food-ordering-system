package com.labs.lg.food.ordering.system.order.service.domain;

import com.labs.lg.food.ordering.system.domain.valueobject.OrderStatus;
import com.labs.lg.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Order;
import com.labs.lg.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.labs.lg.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.labs.lg.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.labs.lg.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.labs.lg.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.labs.lg.food.ordering.system.order.service.domain.outbox.scheduler.approval.ApprovalOutboxHelper;
import com.labs.lg.food.ordering.system.order.service.domain.outbox.scheduler.payment.PaymentOutboxHelper;
import com.labs.lg.food.ordering.system.outbox.OutboxStatus;
import com.labs.lg.food.ordering.system.saga.SagaStatus;
import com.labs.lg.food.ordering.system.saga.SagaStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import static com.labs.lg.pentagon.common.domain.DomainConstants.UTC;

@Slf4j
@Component
public class OrderApprovalSaga implements SagaStep<RestaurantApprovalResponse> {

  private final OrderDomainService orderDomainService;
  private final OrderSagaHelper orderSagaHelper;
  private final ApprovalOutboxHelper approvalOutboxHelper;
  private final PaymentOutboxHelper paymentOutboxHelper;
  private final OrderDataMapper orderDataMapper;

  public OrderApprovalSaga(OrderDomainService orderDomainService,
                           OrderSagaHelper orderSagaHelper,
                           ApprovalOutboxHelper approvalOutboxHelper,
                           PaymentOutboxHelper paymentOutboxHelper,
                           OrderDataMapper orderDataMapper) {
    this.orderDomainService = orderDomainService;
    this.orderSagaHelper = orderSagaHelper;
    this.approvalOutboxHelper = approvalOutboxHelper;
    this.paymentOutboxHelper = paymentOutboxHelper;
    this.orderDataMapper = orderDataMapper;
  }

  /**
   * <h1>OPTIMISTIC LOCKING STRATEGY</h1>
   * 1. Para prevenir que se consuma dos veces el mismo evento... se controla con el Saga Id y el Estatus.
   * <p>
   * 2. Puede ocurrir una condición de carrera, EN el caso, que llege dos veces el mismo evento y al mismo tiempo
   * entonces, serán dos hilos simultaneros preguntando en la base de datos por el Saga ID y con STARTED status.
   * <p>
   * 2.1: En los dos hilos sera valido, pero no ha finalizado ningún hilo primero. <br>
   * 2.2: La solución es aplicar una estrategia en Base de Datos Optimistic Locking.
   * <p>
   * <p>
   * --------------------------    REMEMBER FOR FIXED THE CARRIER CONDITION       --------------------------
   * ______________________________________APPLY OPTIMISTIC LOCKING
   * <p>
   * In this cases, with de entity PaymentOutboxEntity, has a field called VERSION, and this field has
   * a spring annotation <b>@Version</b> So, Hibernate apply Optimistic Locking.
   */
  @Override
  @Transactional
  public void process(RestaurantApprovalResponse restaurantApprovalResponse) {
    // step 1: validate if the event has not been processed

    final Optional<OrderApprovalOutboxMessage> approvalOutboxMessageResponse = approvalOutboxHelper
        .getApprovalOutboxMessageBySagaIdAndSagaStatus(
            UUID.fromString(restaurantApprovalResponse.getSagaId()),
            SagaStatus.PROCESSING);

    if (approvalOutboxMessageResponse.isEmpty()) {
      log.info("An Outbox with saga id: {} is already processed!",
          restaurantApprovalResponse.getSagaId());
      return;
    }
    OrderApprovalOutboxMessage orderApprovalOutboxMessage = approvalOutboxMessageResponse.get();

    // step 2: Start with logic business using the Domain Service

    Order order = approveOrder(restaurantApprovalResponse);

    // step 3: calculate a new saga status with the new order status

    SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(order.getOrderStatus());

    // step 4: Update the OutBox tables, with new state system(Update in Approval and Payment Outbox)

    approvalOutboxHelper.save(getUpdatedApprovalOutboxMessage(orderApprovalOutboxMessage,
        order.getOrderStatus(), sagaStatus));

    paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(restaurantApprovalResponse.getSagaId(),
        order.getOrderStatus(), sagaStatus));

    log.info("Order with id: {} is approved", order.getId().getValue());
  }


  @Override
  @Transactional
  public void rollback(RestaurantApprovalResponse restaurantApprovalResponse) {
    // step 1: validate if the event has not been processed

    final Optional<OrderApprovalOutboxMessage> approvalOutboxMessageResponse = approvalOutboxHelper
        .getApprovalOutboxMessageBySagaIdAndSagaStatus(
            UUID.fromString(restaurantApprovalResponse.getSagaId()),
            SagaStatus.PROCESSING);

    if (approvalOutboxMessageResponse.isEmpty()) {
      log.info("An Outbox with saga id: {} is already processed!",
          restaurantApprovalResponse.getSagaId());
      return;
    }
    OrderApprovalOutboxMessage orderApprovalOutboxMessage = approvalOutboxMessageResponse.get();

    // step 2: Start with logic business using the Domain Service
    OrderCancelledEvent domainEvent = rollbackOrder(restaurantApprovalResponse);

    // step 3: calculate a new saga status with the new order status

    SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(domainEvent.getOrder().getOrderStatus());

    // step 4: Update the OutBox tables, with new state system(Update in Approval)
    /*
     *  Start optimistic locking when try save the object, If it has the same version, os is OK.
     *  And else, throw exception Optimistic Locking exception
     */
    approvalOutboxHelper.save(getUpdatedApprovalOutboxMessage(orderApprovalOutboxMessage,
        domainEvent.getOrder().getOrderStatus(), sagaStatus));

    // step 5: Create a new Payment OutBox object, for the rollback process into payment service
    /*
     * The Payment Outbox has a unique index(type, saga_id, saga_status), is no allowed duplicate objects
     */
    paymentOutboxHelper.savePaymentOutboxMessage(
        orderDataMapper.orderCancelledEventToOrderPaymentEventPayload(domainEvent),
        domainEvent.getOrder().getOrderStatus(),
        sagaStatus,
        OutboxStatus.STARTED,
        UUID.fromString(restaurantApprovalResponse.getSagaId()));

    log.info("Order with id: {} is cancelling", domainEvent.getOrder().getId().getValue());
  }


  private Order approveOrder(RestaurantApprovalResponse restaurantApprovalResponse) {
    log.info("Approving order with id: {}", restaurantApprovalResponse.getOrderId());
    Order order = orderSagaHelper.findOrder(restaurantApprovalResponse.getOrderId());
    orderDomainService.approveOrder(order);
    orderSagaHelper.saveOrder(order);
    return order;
  }

  private OrderApprovalOutboxMessage getUpdatedApprovalOutboxMessage(OrderApprovalOutboxMessage
                                                                         orderApprovalOutboxMessage,
                                                                     OrderStatus
                                                                         orderStatus,
                                                                     SagaStatus
                                                                         sagaStatus) {
    orderApprovalOutboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of(UTC)));
    orderApprovalOutboxMessage.setOrderStatus(orderStatus);
    orderApprovalOutboxMessage.setSagaStatus(sagaStatus);
    return orderApprovalOutboxMessage;
  }

  private OrderPaymentOutboxMessage getUpdatedPaymentOutboxMessage(String sagaId,
                                                                   OrderStatus orderStatus,
                                                                   SagaStatus sagaStatus) {
    // get payment outbox with saga status as processing
    Optional<OrderPaymentOutboxMessage> paymentOutboxMessageResponse = paymentOutboxHelper.getPaymentOutboxMessageBySagaIdAndSagaStatus(
        UUID.fromString(sagaId),
        SagaStatus.PROCESSING);
    if (paymentOutboxMessageResponse.isEmpty()) {
      throw new OrderDomainException("Payment outbox message could not be found in " +
          SagaStatus.PROCESSING.name() + "state");
    }
    OrderPaymentOutboxMessage orderPaymentOutboxMessage = paymentOutboxMessageResponse.get();
    orderPaymentOutboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of(UTC)));
    orderPaymentOutboxMessage.setOrderStatus(orderStatus);
    orderPaymentOutboxMessage.setSagaStatus(sagaStatus);
    return orderPaymentOutboxMessage;
  }

  private OrderCancelledEvent rollbackOrder(RestaurantApprovalResponse restaurantApprovalResponse) {
    log.info("Cancelling order with id: {}", restaurantApprovalResponse.getOrderId());
    Order order = orderSagaHelper.findOrder(restaurantApprovalResponse.getOrderId());

    OrderCancelledEvent domainEvent = orderDomainService
        .cancelOrderPayment(order, restaurantApprovalResponse.getFailureMessages());
    orderSagaHelper.saveOrder(order);
    return domainEvent;
  }

}
