package com.labs.lg.food.ordering.system.customer.service.domain.ports.input.service;

import com.labs.lg.food.ordering.system.customer.service.domain.dto.creator.CreateCustomerCommand;
import com.labs.lg.food.ordering.system.customer.service.domain.dto.creator.CreateCustomerResponse;

import jakarta.validation.Valid;

public interface CustomerApplicationService {
    CreateCustomerResponse createCustomer(@Valid CreateCustomerCommand createCustomerCommand);

}
