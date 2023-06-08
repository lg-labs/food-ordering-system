package com.labs.lg.food.ordering.system.payment.service.domain.exception;


import com.labs.lg.pentagon.common.domain.exception.DomainException;

public class PaymentApplicationServiceException extends DomainException {

  public PaymentApplicationServiceException(String message) {
    super(message);
  }

  public PaymentApplicationServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
