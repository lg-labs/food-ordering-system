package com.labs.lg.food.ordering.system.payment.service.domain.boot;

import com.lg5.spring.testcontainer.config.KafkaContainerCustomConfig;
import com.lg5.spring.testcontainer.config.PostgresContainerCustomConfig;
import org.springframework.context.annotation.Import;

@Import({
        PostgresContainerCustomConfig.class,
        KafkaContainerCustomConfig.class,
})
public final class TestContainersLoader {
}
