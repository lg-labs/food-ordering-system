package com.labs.lg.food.ordering.system.customer.service;

import com.labs.lg.food.ordering.system.customer.service.domain.CustomerDomainService;
import com.labs.lg.food.ordering.system.customer.service.domain.CustomerDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public CustomerDomainService customerDomainService() {
        return new CustomerDomainServiceImpl();
    }
}
