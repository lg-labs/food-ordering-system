package com.labs.lg.food.ordering.system.customer.service.domain.mapper;

import com.labs.lg.food.ordering.system.customer.service.domain.dto.creator.CreateCustomerCommand;
import com.labs.lg.food.ordering.system.customer.service.domain.dto.creator.CreateCustomerResponse;
import com.labs.lg.food.ordering.system.customer.service.domain.entity.Customer;
import com.labs.lg.food.ordering.system.customer.service.domain.valueobject.CustomerId;
import com.labs.lg.food.ordering.system.customer.service.domain.valueobject.Username;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataMapper {
    public Customer createCustomerCommandToCustomer(CreateCustomerCommand createCustomerCommand) {
        return new Customer(
                new CustomerId(createCustomerCommand.customerId()),
                new Username(createCustomerCommand.username()),
                createCustomerCommand.firstName(),
                createCustomerCommand.lastName());
    }

    public CreateCustomerResponse customerToCreateCustomerResponse(Customer customer, String message) {
        return new CreateCustomerResponse(customer.getId().getValue(), message);
    }
}
