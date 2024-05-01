package com.labs.lg.food.ordering.system.customer.service.api.rest;

import com.labs.lg.food.ordering.system.customer.service.domain.dto.creator.CreateCustomerCommand;
import com.labs.lg.food.ordering.system.customer.service.domain.dto.creator.CreateCustomerResponse;
import com.labs.lg.food.ordering.system.customer.service.domain.ports.input.service.CustomerApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/customers", produces = "application/vnd.api.v1+json")
public class CustomerController {
    private final CustomerApplicationService customerApplicationService;

    public CustomerController(CustomerApplicationService customerApplicationService) {
        this.customerApplicationService = customerApplicationService;
    }

    @PostMapping
    public ResponseEntity<Void> createCustomer(@RequestBody CreateCustomerCommand
                                                       createCustomerCommand) {
        log.info("Creating customer with username : {}", createCustomerCommand.username());
        final CreateCustomerResponse createCustomerResponse = customerApplicationService
                .createCustomer(createCustomerCommand);
        log.info("Customer with id: {} was created", createCustomerResponse.customerId());
        return ResponseEntity.accepted().build();
    }
}
