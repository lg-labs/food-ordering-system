package com.labs.lg.food.ordering.system.order.service.domain;

import com.labs.lg.food.ordering.system.order.service.domain.dto.message.CustomerModel;
import com.labs.lg.food.ordering.system.order.service.domain.ports.input.message.listener.customer.CustomerMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerMessageListenerImpl implements CustomerMessageListener {

    private final CustomerHelper customerHelper;

    public CustomerMessageListenerImpl(CustomerHelper customerHelper) {
        this.customerHelper = customerHelper;
    }

    @Override
    public void customerCreated(CustomerModel customerCreatedEvent) {
        customerHelper.persistCustomerCreated(customerCreatedEvent);
    }
}
