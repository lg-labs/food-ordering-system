package com.labs.lg.food.ordering.system.restaurant.service.domain.boot;

import com.labs.lg.food.ordering.system.restaurant.service.domain.RestaurantServiceApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration(proxyBeanMethods = false)
@Import(TestContainersLoader.class)
class TestRestaurantServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(RestaurantServiceApplication::main)
                .with(TestRestaurantServiceApplication.class)
                .run(args);
    }
}
