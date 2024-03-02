package com.labs.lg.food.ordering.system.customer.service.domain.entity;

import com.labs.lg.food.ordering.system.customer.service.domain.exception.CustomerDomainException;
import com.labs.lg.food.ordering.system.domain.valueobject.CustomerId;
import com.labs.lg.food.ordering.system.domain.valueobject.Username;
import com.labs.lg.pentagon.common.domain.entity.AggregateRoot;

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
     * */
    public void validate(){
     if (username == null || getId() == null){
         throw new CustomerDomainException("The customer to create is invalid");
     }
    }

}
