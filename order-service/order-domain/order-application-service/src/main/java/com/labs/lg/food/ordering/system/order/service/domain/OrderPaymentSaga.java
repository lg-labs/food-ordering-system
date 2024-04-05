package com.labs.lg.food.ordering.system.order.service.domain;

import com.labs.lg.food.ordering.system.domain.valueobject.OrderStatus;
import com.labs.lg.food.ordering.system.domain.valueobject.PaymentStatus;
import com.labs.lg.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Order;
import com.labs.lg.food.ordering.system.order.service.domain.event.OrderPaidEvent;
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
public class OrderPaymentSaga implements SagaStep<PaymentResponse> {

    private final OrderDomainService orderDomainService;
    private final OrderSagaHelper orderSagaHelper;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final ApprovalOutboxHelper approvalOutboxHelper;
    private final OrderDataMapper orderDataMapper;

    public OrderPaymentSaga(OrderDomainService orderDomainService,
                            OrderSagaHelper orderSagaHelper,
                            PaymentOutboxHelper paymentOutboxHelper,
                            ApprovalOutboxHelper approvalOutboxHelper,
                            OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderSagaHelper = orderSagaHelper;
        this.paymentOutboxHelper = paymentOutboxHelper;
        this.approvalOutboxHelper = approvalOutboxHelper;
        this.orderDataMapper = orderDataMapper;
    }

    /**
     * <h1>OPTIMISTIC LOCKING STRATEGY</h1>
     * 1. Para prevenir que se consuma dos veces el mismo evento... se controla con el Saga Id y el Estatus.
     * <p>
     * 2. Puede ocurrir una condición de carrera, En el caso, que llege dos veces el mismo evento y al mismo tiempo
     * entonces, serán dos hilos simultaneros preguntando en la base de datos por el Saga ID y con STARTED status.
     * <p>
     * 2.1: En los dos hilos sera valido, pero no ha finalizado ningún hilo primero. <br/>
     * 2.2: La solución es aplicar una estrategia en Base de Datos Optimistic Locking.
     * <p>
     * <p>
     * --------------------------    REMEMBER FOR FIXED THE CARRIER CONDITION       --------------------------
     * ______________________________________APPLY OPTIMISTIC LOCKING
     * <p>
     * In this cases, with de entity PaymentOutboxEntity has a field called VERSION, and this field has
     * a spring annotation <b>@Version</b> So, Hibernate apply Optimistic Locking.
     */
    @Override
    @Transactional
    public void process(PaymentResponse paymentResponse) {
        final Optional<OrderPaymentOutboxMessage> orderPaymentOutboxMessagesResponse = paymentOutboxHelper
                .getPaymentOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(paymentResponse.getSagaId()),
                        SagaStatus.STARTED
                );

        if (orderPaymentOutboxMessagesResponse.isEmpty()) {
            log.info("An outbox message with saga id: {} is already processed!", paymentResponse.getSagaId());
            return;
        }

        final OrderPaymentOutboxMessage orderPaymentOutboxMessage = orderPaymentOutboxMessagesResponse.get();

        final OrderPaidEvent domainEvent = completePaymentForOrder(paymentResponse);


        final SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(domainEvent.getOrder().getOrderStatus());
        paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(orderPaymentOutboxMessage, domainEvent.getOrder().getOrderStatus(), sagaStatus));

        approvalOutboxHelper.saveApprovalOutboxMessage(
                orderDataMapper.orderPaidEventToOrderApprovalEventPayload(domainEvent),
                domainEvent.getOrder().getOrderStatus(),
                sagaStatus,
                OutboxStatus.STARTED,
                UUID.fromString(paymentResponse.getSagaId()));

        log.info("Order with id:{} is paid", domainEvent.getOrder().getId().getValue());
    }


    @Override
    @Transactional
    public void rollback(PaymentResponse paymentResponse) {
        final Optional<OrderPaymentOutboxMessage> orderPaymentOutboxMessagesResponse = paymentOutboxHelper
                .getPaymentOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(paymentResponse.getSagaId()),
                        getCurrentSagaStatus(paymentResponse.getPaymentStatus())
                );

        if (orderPaymentOutboxMessagesResponse.isEmpty()) {
            log.info("An outbox message with saga id: {} is already roll backed!", paymentResponse.getSagaId());
            return;
        }

        final OrderPaymentOutboxMessage orderPaymentOutboxMessage = orderPaymentOutboxMessagesResponse.get();

        final Order order = rollbackPaymentForOrder(paymentResponse);

        final SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(order.getOrderStatus());

        paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(orderPaymentOutboxMessage,
                order.getOrderStatus(), sagaStatus));

        if (paymentResponse.getPaymentStatus() == PaymentStatus.CANCELLED) {
            approvalOutboxHelper.save(getUpdatedApprovalOutboxMessage(paymentResponse.getSagaId(),
                    order.getOrderStatus(), sagaStatus));
        }

        log.info("Order with id:{} is cancelled", order.getId().getValue());
    }

    private OrderPaymentOutboxMessage getUpdatedPaymentOutboxMessage(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
                                                                     OrderStatus orderStatus,
                                                                     SagaStatus sagaStatus) {
        orderPaymentOutboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of(UTC)));
        orderPaymentOutboxMessage.setOrderStatus(orderStatus);
        orderPaymentOutboxMessage.setSagaStatus(sagaStatus);

        return orderPaymentOutboxMessage;
    }

    OrderPaidEvent completePaymentForOrder(PaymentResponse paymentResponse) {
        final String orderId = paymentResponse.getOrderId();
        log.info("Completing payment for order id: {}", orderId);
        final Order order = orderSagaHelper.findOrder(orderId);
        final OrderPaidEvent domainEvent = orderDomainService.payOrder(order);
        orderSagaHelper.saveOrder(order);
        return domainEvent;
    }

    private SagaStatus[] getCurrentSagaStatus(PaymentStatus paymentStatus) {
        return switch (paymentStatus) {
            case COMPLETED -> new SagaStatus[]{SagaStatus.STARTED};
            case CANCELLED -> new SagaStatus[]{SagaStatus.PROCESSING};
            case FAILED -> new SagaStatus[]{SagaStatus.STARTED, SagaStatus.PROCESSING};
        };
    }

    private Order rollbackPaymentForOrder(PaymentResponse paymentResponse) {
        final String orderId = paymentResponse.getOrderId();
        log.info("Cancelling order with id: {}", orderId);
        final Order order = orderSagaHelper.findOrder(orderId);
        orderDomainService.cancelOrder(order, paymentResponse.getFailureMessages());
        orderSagaHelper.saveOrder(order);
        return order;
    }

    private OrderApprovalOutboxMessage getUpdatedApprovalOutboxMessage(String sagaId,
                                                                       OrderStatus orderStatus,
                                                                       SagaStatus sagaStatus) {
        final Optional<OrderApprovalOutboxMessage> orderApprovalOutboxMessageResponse =
                approvalOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(sagaId), SagaStatus.COMPENSATING);
        if (orderApprovalOutboxMessageResponse.isEmpty()) {
            throw new OrderDomainException("Approval outbox message could not be found in "
                    + SagaStatus.COMPENSATING.name() + "status!");
        }
        final OrderApprovalOutboxMessage orderApprovalOutboxMessage = orderApprovalOutboxMessageResponse.get();
        orderApprovalOutboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of(UTC)));
        orderApprovalOutboxMessage.setOrderStatus(orderStatus);
        orderApprovalOutboxMessage.setSagaStatus(sagaStatus);
        return orderApprovalOutboxMessage;
    }

}
