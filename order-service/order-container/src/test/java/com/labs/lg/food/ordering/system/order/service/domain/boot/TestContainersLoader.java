package com.labs.lg.food.ordering.system.order.service.domain.boot;

import com.lg5.spring.testcontainer.config.KafkaContainerCustomConfig;
import com.lg5.spring.testcontainer.config.PostgresContainerCustomConfig;
import com.lg5.spring.testcontainer.config.WiremockContainerCustomConfig;
import org.springframework.context.annotation.Import;

@Import({
        PostgresContainerCustomConfig.class,
        KafkaContainerCustomConfig.class,
        WiremockContainerCustomConfig.class,
})
public final class TestContainersLoader {
}
