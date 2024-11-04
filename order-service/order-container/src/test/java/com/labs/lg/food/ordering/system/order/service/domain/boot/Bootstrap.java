package com.labs.lg.food.ordering.system.order.service.domain.boot;

import com.lg5.spring.integration.test.boot.Lg5TestBoot;
import org.springframework.context.annotation.Import;

@Import(TestContainersLoader.class)
public abstract class Bootstrap extends Lg5TestBoot {
}
