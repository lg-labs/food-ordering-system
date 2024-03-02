package com.labs.lg.food.ordering.system.customer.service.domain;

import com.labs.lg.food.ordering.system.customer.service.domain.dto.creator.CreateCustomerCommand;
import com.labs.lg.food.ordering.system.customer.service.domain.dto.creator.CreateCustomerResponse;
import com.labs.lg.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent;
import com.labs.lg.food.ordering.system.customer.service.domain.mapper.CustomerDataMapper;
import com.labs.lg.food.ordering.system.customer.service.domain.ports.input.service.CustomerApplicationService;
import com.labs.lg.food.ordering.system.customer.service.domain.ports.output.message.publisher.CustomerMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


@Slf4j
@Validated
@Service
public class CustomerApplicationServiceImpl implements CustomerApplicationService {


    private final CustomerCreateCommandHandler customerCreateCommandHandler;

    private final CustomerDataMapper customerDataMapper;

    private final CustomerMessagePublisher customerMessagePublisher;

    public CustomerApplicationServiceImpl(CustomerCreateCommandHandler customerCreateCommandHandler,
                                          CustomerDataMapper customerDataMapper,
                                          CustomerMessagePublisher customerMessagePublisher) {
        this.customerCreateCommandHandler = customerCreateCommandHandler;
        this.customerDataMapper = customerDataMapper;
        this.customerMessagePublisher = customerMessagePublisher;
    }

    /**
     * TODO:
     * still need to outbox pattern because, after persisting the data(customer)
     * into database, i cannot be sure if the publish operation will be successful or if it is not successful.
     *
     * I will have inconsistency😳, between the customer tables in customer service and others services.
     *
     * @param createCustomerCommand
     * @return
     */
    @Override
    public CreateCustomerResponse createCustomer(CreateCustomerCommand createCustomerCommand) {
        CustomerCreatedEvent customerCreatedEvent = customerCreateCommandHandler.createCustomer(createCustomerCommand);
        customerMessagePublisher.publish(customerCreatedEvent);
        return customerDataMapper
                .customerToCreateCustomerResponse(customerCreatedEvent.getCustomer(),
                        "Customer saved successfully!");
    }
}
