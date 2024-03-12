package com.labs.lg.food.ordering.system.order.service.messaging.listener.kafka;

import com.labs.lg.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.labs.lg.food.ordering.system.kafka.order.avro.model.CustomerAvroModel;
import com.labs.lg.food.ordering.system.order.service.domain.exception.OrderApplicationServiceException;
import com.labs.lg.food.ordering.system.order.service.domain.ports.input.message.listener.customer.CustomerMessageListener;
import com.labs.lg.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class CustomerKafkaListener implements KafkaConsumer<CustomerAvroModel> {

    private final CustomerMessageListener customerMessageListener;
    private final OrderMessagingDataMapper mapper;

    public CustomerKafkaListener(final CustomerMessageListener customerMessageListener,
                                 final OrderMessagingDataMapper mapper) {
        this.customerMessageListener = customerMessageListener;
        this.mapper = mapper;
    }

    @Override
    @KafkaListener(
            id = "${kafka-consumer-config.customer-consumer-group-id}",
            topics = "${order-service.customer-topic-name}"
    )
    public void receive(@Payload List<CustomerAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of customer create messages received with keys {}, partitions {} and offsets {}" +
                        ", sending for customer",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(customerAvroModel -> {
            try {
                log.info("Processing customer created for id: {}", customerAvroModel.getId());
                customerMessageListener.customerCreated(mapper
                        .customerAvroModelToCustomerModel(customerAvroModel));
            } catch (Exception e) {
                throw new OrderApplicationServiceException("Throwing DataAccessException in" +
                        " CustomerKafkaListener: " + e.getMessage(), e);
            }
        });
    }
}
