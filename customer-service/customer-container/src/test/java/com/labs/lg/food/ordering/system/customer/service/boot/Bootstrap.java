package com.labs.lg.food.ordering.system.customer.service.boot;

import com.lg5.spring.integration.test.boot.Lg5TestBoot;
import org.springframework.context.annotation.Import;

@Import(TestContainersLoader.class)
public abstract class Bootstrap extends Lg5TestBoot {
}