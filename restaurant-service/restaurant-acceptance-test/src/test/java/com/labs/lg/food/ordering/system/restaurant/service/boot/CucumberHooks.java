package com.labs.lg.food.ordering.system.restaurant.service.boot;

import com.lg5.spring.integration.test.boot.Lg5TestBootPortNone;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.context.annotation.Import;

@Import(TestContainersLoader.class)
@CucumberContextConfiguration
public final class CucumberHooks extends Lg5TestBootPortNone {

}
