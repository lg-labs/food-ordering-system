package com.labs.lg.food.ordering.system.order.service.domain;

import org.springframework.context.annotation.Bean;

public class BeanConfiguration {

    @Bean
    public OrderDomainService orderDomainService(){
        return new OrderDomainServiceIml();
    }
}
