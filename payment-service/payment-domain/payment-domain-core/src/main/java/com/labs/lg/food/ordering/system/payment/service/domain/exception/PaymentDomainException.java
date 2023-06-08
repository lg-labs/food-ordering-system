package com.labs.lg.food.ordering.system.payment.service.domain.exception;

import com.labs.lg.pentagon.common.domain.exception.DomainException;

public class PaymentDomainException extends DomainException {

  protected PaymentDomainException(String message) {
    super(message);
  }

  protected PaymentDomainException(String message, Throwable cause) {
    super(message, cause);
  }
}
