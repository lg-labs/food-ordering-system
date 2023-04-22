package com.labs.lg.food.ordering.system.order.service.domain.ports.input.message.listener.payment;

import com.labs.lg.food.ordering.system.order.service.domain.dto.message.PaymentResponse;

public interface PaymentResponseMessageListener {

  void paymentCompleted(PaymentResponse paymentResponse);

  /**
   * <h2>SAGA Rollback Operation</h2>
   * This payment canceled methods can be called in case payment is failed.
   * Because of a business logic invariant but it can be a response to the
   * payment cancel request as part of the  SAGA ROLLBACK operation.
   * @param paymentResponse it will have a failure messages
   */
  void paymentCancelled(PaymentResponse paymentResponse);
}
