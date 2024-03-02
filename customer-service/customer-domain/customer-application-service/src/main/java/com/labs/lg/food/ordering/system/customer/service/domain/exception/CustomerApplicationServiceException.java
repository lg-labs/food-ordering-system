package com.labs.lg.food.ordering.system.customer.service.domain.exception;

import com.labs.lg.pentagon.common.domain.exception.DomainException;

public class CustomerApplicationServiceException extends DomainException {
    public CustomerApplicationServiceException(String message) {
        super(message);
    }

    public CustomerApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
