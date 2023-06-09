package com.labs.lg.food.ordering.system.restaurant.service.domain.exception;

import com.labs.lg.pentagon.common.domain.exception.DomainException;

public class RestaurantApplicationServiceException extends DomainException {
  protected RestaurantApplicationServiceException(String message) {
    super(message);
  }

  protected RestaurantApplicationServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
