package com.labs.lg.food.ordering.system.customer.service.domain;

import com.labs.lg.food.ordering.system.customer.service.domain.entity.Customer;
import com.labs.lg.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.labs.lg.pentagon.common.domain.DomainConstants.UTC;

public class CustomerDomainServiceImpl implements CustomerDomainService {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerDomainServiceImpl.class);

    @Override
    public CustomerCreatedEvent validateAndInitiateCustomer(Customer customer) {
        //Any Business logic required to run for a customer creation
        customer.validate();
        LOG.info("Customer with id: {} is initiated", customer.getId().getValue());
        return new CustomerCreatedEvent(customer, ZonedDateTime.now(ZoneId.of(UTC)));
    }
}
