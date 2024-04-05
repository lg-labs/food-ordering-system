package com.labs.lg.food.ordering.system.customer.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * ::: -- NOTA  --  :::
 * This annotation are important when we have jpa repositories and entities on DIFFERENT MODULES
 */
@EnableJpaRepositories(basePackages = {
        "com.labs.lg.food.ordering.system.customer.service.dataaccess",
        "com.labs.lg.food.ordering.system.dataaccess"
})
@EntityScan(basePackages = {
        "com.labs.lg.food.ordering.system.customer.service.dataaccess",
        "com.labs.lg.food.ordering.system.dataaccess"
})
@SpringBootApplication(scanBasePackages = "com.labs.lg.food.ordering.system")
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
}
