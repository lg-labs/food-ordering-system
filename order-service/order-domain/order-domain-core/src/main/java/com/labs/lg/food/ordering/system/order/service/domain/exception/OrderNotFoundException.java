package com.labs.lg.food.ordering.system.order.service.domain.exception;


import com.labs.lg.pentagon.common.domain.exception.DomainException;

public class OrderNotFoundException extends DomainException {

    public OrderNotFoundException(String message) {
        super(message);
    }
}
