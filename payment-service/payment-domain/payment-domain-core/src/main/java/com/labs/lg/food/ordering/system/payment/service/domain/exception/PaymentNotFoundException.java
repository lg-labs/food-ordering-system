package com.labs.lg.food.ordering.system.payment.service.domain.exception;

import com.labs.lg.pentagon.common.domain.exception.DomainException;

public class PaymentNotFoundException extends DomainException {

  protected PaymentNotFoundException(String message) {
    super(message);
  }
}
