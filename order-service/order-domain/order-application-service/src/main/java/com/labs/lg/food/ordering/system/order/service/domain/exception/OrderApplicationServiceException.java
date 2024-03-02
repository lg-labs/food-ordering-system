package com.labs.lg.food.ordering.system.order.service.domain.exception;

import com.labs.lg.pentagon.common.domain.exception.DomainException;

public class OrderApplicationServiceException extends DomainException {

    public OrderApplicationServiceException(String message) {
        super(message);
    }

    public OrderApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
