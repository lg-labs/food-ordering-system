package com.labs.lg.food.ordering.system.customer.service.messaging.mapper;

import com.labs.lg.food.ordering.system.customer.service.domain.event.CustomerEvent;
import com.labs.lg.food.ordering.system.kafka.order.avro.model.CustomerAvroModel;
import org.springframework.stereotype.Component;

@Component
public class CustomerMessagingDataMapper {

    public CustomerAvroModel customerCreatedEventToCustomerRequestAvroModel(CustomerEvent customerEvent) {
        return CustomerAvroModel.newBuilder()
                .setId(customerEvent.getCustomer().getId().getValue().toString())
                .setUsername(customerEvent.getCustomer().getUsername().getValue())
                .setFirstName(customerEvent.getCustomer().getFirstName())
                .setLastName(customerEvent.getCustomer().getLastName())
                .build();
    }
}
