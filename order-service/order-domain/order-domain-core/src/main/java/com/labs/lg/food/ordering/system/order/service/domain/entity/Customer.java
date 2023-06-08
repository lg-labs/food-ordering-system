package com.labs.lg.food.ordering.system.order.service.domain.entity;

import com.labs.lg.pentagon.common.domain.entity.AggregateRoot;
import com.labs.lg.food.ordering.system.domain.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {
    public Customer() {
    }

    public Customer(CustomerId customerId) {
        setId(customerId);
    }
}
