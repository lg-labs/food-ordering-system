package com.labs.lg.food.ordering.system.restaurant.service.domain.exception;

import com.labs.lg.pentagon.common.domain.exception.DomainException;

public class RestaurantDomainException extends DomainException {
  protected RestaurantDomainException(String message) {
    super(message);
  }

  protected RestaurantDomainException(String message, Throwable cause) {
    super(message, cause);
  }
}
