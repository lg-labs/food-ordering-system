package com.labs.lg.food.ordering.system.customer.service.domain;

import com.labs.lg.food.ordering.system.customer.service.domain.dto.creator.CreateCustomerCommand;
import com.labs.lg.food.ordering.system.customer.service.domain.entity.Customer;
import com.labs.lg.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent;
import com.labs.lg.food.ordering.system.customer.service.domain.exception.CustomerDomainException;
import com.labs.lg.food.ordering.system.customer.service.domain.mapper.CustomerDataMapper;
import com.labs.lg.food.ordering.system.customer.service.domain.ports.output.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class CustomerCreateCommandHandler {

    private final CustomerDomainService customerDomainService;
    private final CustomerRepository customerRepository;
    private final CustomerDataMapper customerDataMapper;


    public CustomerCreateCommandHandler(CustomerDomainService customerDomainService,
                                        CustomerRepository customerRepository,
                                        CustomerDataMapper customerDataMapper) {
        this.customerDomainService = customerDomainService;
        this.customerRepository = customerRepository;
        this.customerDataMapper = customerDataMapper;
    }

    @Transactional
    public CustomerCreatedEvent createCustomer(CreateCustomerCommand createCustomerCommand) {
        final Customer customer = customerDataMapper.createCustomerCommandToCustomer(createCustomerCommand);
        final CustomerCreatedEvent customerCreatedEvent = customerDomainService.validateAndInitiateCustomer(customer);
        final Customer savedCustomer = customerRepository.createCustomer(customer);
        if (savedCustomer == null) {
            log.error("Could not save customer with id: {}", createCustomerCommand.customerId());
            throw new CustomerDomainException("Could not save customer with id "
                    + createCustomerCommand.customerId());
        }
        log.info("Returning CustomerCreatedEvent for customer id: {}", createCustomerCommand.customerId());
        return customerCreatedEvent;
    }
}
