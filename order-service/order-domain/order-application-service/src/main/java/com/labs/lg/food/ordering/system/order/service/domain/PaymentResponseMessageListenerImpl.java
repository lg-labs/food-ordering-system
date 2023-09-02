package com.labs.lg.food.ordering.system.order.service.domain;

import com.labs.lg.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.labs.lg.food.ordering.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.labs.lg.food.ordering.system.order.service.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;

/**
 * <h2>SAGA Rollback Operation</h2>
 * This payment canceled methods can be called in case payment is failed.
 * Because of a business logic invariant but it can be a response to the
 * payment cancel request as part of the  SAGA ROLLBACK operation.
 */
@Slf4j
@Validated
@Service
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {

  private final OrderPaymentSaga orderPaymentSaga;

  public PaymentResponseMessageListenerImpl(OrderPaymentSaga orderPaymentSaga) {
    this.orderPaymentSaga = orderPaymentSaga;
  }

  @Override
  public void paymentCompleted(PaymentResponse paymentResponse) {
    orderPaymentSaga.process(paymentResponse);
    log.info("Order Payment Saga process operation is completed for order id: {}", paymentResponse.getOrderId());
  }

  /**
   * <h2>SAGA Rollback Operation</h2>
   * This payment canceled methods can be called in case payment is failed.
   * Because of a business logic invariant but it can be a response to the
   * payment cancel request as part of the  SAGA ROLLBACK operation.
   * @param paymentResponse it will have a failure messages
   */
  @Override
  public void paymentCancelled(PaymentResponse paymentResponse) {
   orderPaymentSaga.rollback(paymentResponse);
   log.info("Order is roll backed for order id: {} with failure message: {} ",
       paymentResponse.getOrderId(),
       String.join(FAILURE_MESSAGE_DELIMITER, paymentResponse.getFailureMessages()));
  }
}
