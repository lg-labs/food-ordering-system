package com.labs.lg.food.ordering.system.order.service.domain.entity;

import com.labs.lg.food.ordering.system.domain.valueobject.Username;
import com.labs.lg.pentagon.common.domain.entity.AggregateRoot;
import com.labs.lg.food.ordering.system.domain.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {
    private Username username;
    private String firstName;
    private String lastName;

    public Customer(CustomerId customerId, Username username, String firstName, String lastName) {
        super.setId(customerId);
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Customer(CustomerId customerId) {
        setId(customerId);
    }

    public Username getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
