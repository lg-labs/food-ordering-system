package com.labs.lg.food.ordering.system.payment.service.domain.boot;

import com.lg5.spring.integration.test.boot.Lg5TestBootPortNone;
import org.springframework.context.annotation.Import;

@Import(TestContainersLoader.class)
public abstract class Bootstrap extends Lg5TestBootPortNone {
}
