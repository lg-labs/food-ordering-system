package com.labs.lg.food.ordering.system.payment.service.domain;

import com.lg5.spring.testcontainer.TestContainerNotReusedConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration(proxyBeanMethods = false)
@Import({TestContainerNotReusedConfig.class})
public class TestPaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(PaymentServiceApplication::main)
                .with(TestPaymentServiceApplication.class)
                .run(args);
    }
}
