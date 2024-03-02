package com.labs.lg.food.ordering.system.customer.service.domain.ports.output.repository;

import com.labs.lg.food.ordering.system.customer.service.domain.entity.Customer;

public interface CustomerRepository {

    Customer createCustomer(Customer customer);
}
