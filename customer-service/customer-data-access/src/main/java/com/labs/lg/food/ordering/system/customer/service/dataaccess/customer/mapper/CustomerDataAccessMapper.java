package com.labs.lg.food.ordering.system.customer.service.dataaccess.customer.mapper;

import com.labs.lg.food.ordering.system.customer.service.dataaccess.customer.entity.CustomerEntity;
import com.labs.lg.food.ordering.system.customer.service.domain.entity.Customer;
import com.labs.lg.food.ordering.system.customer.service.domain.valueobject.CustomerId;
import com.labs.lg.food.ordering.system.customer.service.domain.valueobject.Username;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {

    public Customer customerEntityToCustomer(CustomerEntity customerEntity) {
        return new Customer(
                new CustomerId(customerEntity.getId()),
                new Username(customerEntity.getUsername()),
                customerEntity.getFirstName(),
                customerEntity.getFirstName());
    }

    public CustomerEntity customerToCustomerEntity(Customer customer) {
        return CustomerEntity.builder()
                .id(customer.getId().getValue())
                .username(customer.getUsername().getValue())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .build();
    }


}
