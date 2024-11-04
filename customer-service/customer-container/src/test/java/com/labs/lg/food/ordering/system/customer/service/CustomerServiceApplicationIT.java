package com.labs.lg.food.ordering.system.customer.service;

import com.labs.lg.food.ordering.system.customer.service.boot.Bootstrap;
import com.labs.lg.food.ordering.system.customer.service.dataaccess.customer.entity.CustomerEntity;
import com.labs.lg.food.ordering.system.customer.service.dataaccess.customer.repository.CustomerJPARepository;
import com.labs.lg.food.ordering.system.customer.service.domain.entity.Customer;
import com.labs.lg.food.ordering.system.customer.service.domain.ports.output.repository.CustomerRepository;
import com.labs.lg.food.ordering.system.customer.service.domain.valueobject.CustomerId;
import com.labs.lg.food.ordering.system.customer.service.domain.valueobject.Username;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerServiceApplicationIT extends Bootstrap {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerJPARepository customerJPARepository;

    @Test
    void it_should_create_a_customer() {
        // given
        final Customer customer = new Customer(new CustomerId(UUID.randomUUID()), new Username("username"), "firstname", "lastname");
        // when
        customerRepository.createCustomer(customer);
        // then
        final Optional<CustomerEntity> customerEntityOptional = customerJPARepository.findById(customer.getId().getValue());
        assertTrue(customerEntityOptional.isPresent());
        CustomerEntity customerEntity = customerEntityOptional.get();
        assertEquals(customer.getId().getValue(), customerEntity.getId());
    }
}