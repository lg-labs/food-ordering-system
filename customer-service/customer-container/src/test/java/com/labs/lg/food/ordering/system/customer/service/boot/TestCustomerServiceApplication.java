package com.labs.lg.food.ordering.system.customer.service.boot;

import com.labs.lg.food.ordering.system.customer.service.CustomerServiceApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration(proxyBeanMethods = false)
@Import(TestContainersLoader.class)
class TestCustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(CustomerServiceApplication::main).with(TestCustomerServiceApplication.class).run(args);
    }
}