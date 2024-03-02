package com.labs.lg.food.ordering.system.order.service.domain.ports.input.message.listener.customer;

import com.labs.lg.food.ordering.system.order.service.domain.dto.message.CustomerModel;

public interface CustomerMessageListener {

    void customerCreated(CustomerModel customerCreatedEvent);
}
