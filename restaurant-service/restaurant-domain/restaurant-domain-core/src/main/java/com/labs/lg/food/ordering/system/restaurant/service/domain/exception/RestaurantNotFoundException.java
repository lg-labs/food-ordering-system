package com.labs.lg.food.ordering.system.restaurant.service.domain.exception;

import com.labs.lg.pentagon.common.domain.exception.DomainException;

public class RestaurantNotFoundException extends DomainException {
    public RestaurantNotFoundException(String message) {
        super(message);
    }

    protected RestaurantNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
