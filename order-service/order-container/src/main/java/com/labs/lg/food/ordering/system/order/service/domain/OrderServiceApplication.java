package com.labs.lg.food.ordering.system.order.service.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = {
        "com.labs.lg.food.ordering.system.order.service.dataaccess.*"
})
@EntityScan(basePackages = {
        "com.labs.lg.food.ordering.system.order.service.dataaccess",
        "com.labs.lg.food.ordering.system.dataaccess"}
)
@SpringBootApplication(scanBasePackages = {"com.labs.lg.food.ordering.system", "com.lg5.spring.kafka", "com.lg5.spring.outbox"})
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
