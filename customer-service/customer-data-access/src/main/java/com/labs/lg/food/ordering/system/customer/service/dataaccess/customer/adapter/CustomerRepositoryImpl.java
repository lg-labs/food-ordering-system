package com.labs.lg.food.ordering.system.customer.service.dataaccess.customer.adapter;

import com.labs.lg.food.ordering.system.customer.service.dataaccess.customer.mapper.CustomerDataAccessMapper;
import com.labs.lg.food.ordering.system.customer.service.dataaccess.customer.repository.CustomerJPARepository;
import com.labs.lg.food.ordering.system.customer.service.domain.entity.Customer;
import com.labs.lg.food.ordering.system.customer.service.domain.ports.output.repository.CustomerRepository;
import org.springframework.stereotype.Component;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {
    private final CustomerJPARepository repository;
    private final CustomerDataAccessMapper customerDataAccessMapper;


    public CustomerRepositoryImpl(CustomerJPARepository repository,
                                  CustomerDataAccessMapper customerDataAccessMapper) {
        this.repository = repository;
        this.customerDataAccessMapper = customerDataAccessMapper;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return customerDataAccessMapper.customerEntityToCustomer(
                repository.save(customerDataAccessMapper.customerToCustomerEntity(customer)));
    }
}
