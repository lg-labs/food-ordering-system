package com.labs.lg.food.ordering.system.customer.service.domain.entity;

import com.labs.lg.food.ordering.system.customer.service.domain.exception.CustomerDomainException;
import com.labs.lg.food.ordering.system.customer.service.domain.valueobject.CustomerId;
import com.labs.lg.food.ordering.system.customer.service.domain.valueobject.Username;
import com.labs.lg.pentagon.common.domain.entity.AggregateRoot;

import java.util.Objects;

public class Customer extends AggregateRoot<CustomerId> {
    private final Username username;
    private final String firstName;
    private final String lastName;

    public Customer(CustomerId customerId, Username username, String firstName, String lastName) {
        super.setId(customerId);
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
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

    /**
     * My Business Logic
     */
    public void validate() {
        if (username == null || getId() == null) {
            throw new CustomerDomainException("The customer to create is invalid");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final var customer = (Customer) o;
        return Objects.equals(username, customer.username)
                && Objects.equals(firstName, customer.firstName)
                && Objects.equals(lastName, customer.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstName, lastName);
    }
}
