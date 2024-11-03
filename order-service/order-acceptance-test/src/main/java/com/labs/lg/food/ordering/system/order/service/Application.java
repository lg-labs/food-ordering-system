package com.labs.lg.food.ordering.system.order.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.labs.lg.food.ordering.system", "com.lg5.spring.kafka"})
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}
