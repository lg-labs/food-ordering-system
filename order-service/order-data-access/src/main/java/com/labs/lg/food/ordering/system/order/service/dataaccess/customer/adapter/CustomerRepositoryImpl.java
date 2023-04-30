package com.labs.lg.food.ordering.system.order.service.dataaccess.customer.adapter;

import com.labs.lg.food.ordering.system.order.service.dataaccess.customer.mapper.CustomerDataAccessMapper;
import com.labs.lg.food.ordering.system.order.service.dataaccess.customer.repository.CustomerJPARepository;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Customer;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * <h1>DATA LAYER ADAPTER</h1>
 * <h2>Secondary Adapter</h2>
 * <p> Implements Data Interface <strong>(Output Port)</strong> from Domain Layer </p>
 *
 * @see CustomerRepository
 */
@Component
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJPARepository customerJPARepository;
    private final CustomerDataAccessMapper customerDataAccessMapper;

    public CustomerRepositoryImpl(CustomerJPARepository customerJPARepository, CustomerDataAccessMapper customerDataAccessMapper) {
        this.customerJPARepository = customerJPARepository;
        this.customerDataAccessMapper = customerDataAccessMapper;
    }


    @Override
    public Optional<Customer> findCustomer(UUID customerId) {
        return customerJPARepository.findById(customerId)
                .map(customerDataAccessMapper::customerEntityToCustomer);
    }
}
