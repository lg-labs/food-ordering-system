package com.labs.lg.food.ordering.system.customer.service.domain.config;

import com.labs.lg.food.ordering.system.customer.service.domain.CustomerDomainService;
import com.labs.lg.food.ordering.system.customer.service.domain.CustomerDomainServiceImpl;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "customer-service")
public class CustomerServiceConfigData {
    private String customerTopicName;

}
