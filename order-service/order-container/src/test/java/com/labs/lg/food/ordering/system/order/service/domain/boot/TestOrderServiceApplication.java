package com.labs.lg.food.ordering.system.order.service.domain.boot;

import com.labs.lg.food.ordering.system.order.service.domain.OrderServiceApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration(proxyBeanMethods = false)
@Import(TestContainersLoader.class)
public class TestOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication
                .from(OrderServiceApplication::main)
                .with(TestOrderServiceApplication.class)
                .run(args);
    }
}
