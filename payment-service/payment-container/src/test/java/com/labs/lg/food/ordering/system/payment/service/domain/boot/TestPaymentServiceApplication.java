package com.labs.lg.food.ordering.system.payment.service.domain.boot;

import com.labs.lg.food.ordering.system.payment.service.domain.PaymentServiceApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration(proxyBeanMethods = false)
@Import(TestContainersLoader.class)
public class TestPaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(PaymentServiceApplication::main)
                .with(TestPaymentServiceApplication.class)
                .run(args);
    }
}
