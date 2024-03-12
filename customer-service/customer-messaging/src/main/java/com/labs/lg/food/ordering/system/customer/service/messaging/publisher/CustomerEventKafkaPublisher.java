package com.labs.lg.food.ordering.system.customer.service.messaging.publisher;

import com.labs.lg.food.ordering.system.customer.service.domain.config.CustomerServiceConfigData;
import com.labs.lg.food.ordering.system.customer.service.domain.entity.Customer;
import com.labs.lg.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent;
import com.labs.lg.food.ordering.system.customer.service.domain.ports.output.message.publisher.CustomerMessagePublisher;
import com.labs.lg.food.ordering.system.customer.service.messaging.mapper.CustomerMessagingDataMapper;
import com.labs.lg.food.ordering.system.domain.valueobject.CustomerId;
import com.labs.lg.food.ordering.system.kafka.order.avro.model.CustomerAvroModel;
import com.labs.lg.food.ordering.system.kafka.producer.service.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Slf4j
@Component
public class CustomerEventKafkaPublisher implements CustomerMessagePublisher {

    private final CustomerMessagingDataMapper customerMessagingDataMapper;
    private final KafkaProducer<String, CustomerAvroModel> kafkaProducer;
    private final CustomerServiceConfigData customerServiceConfigData;

    public CustomerEventKafkaPublisher(CustomerMessagingDataMapper customerMessagingDataMapper,
                                       KafkaProducer<String, CustomerAvroModel> kafkaProducer,
                                       CustomerServiceConfigData customerServiceConfigData) {
        this.customerMessagingDataMapper = customerMessagingDataMapper;
        this.kafkaProducer = kafkaProducer;
        this.customerServiceConfigData = customerServiceConfigData;
    }

    @Override
    public void publish(CustomerCreatedEvent customerCreatedEvent) {
        Customer customer = customerCreatedEvent.getCustomer();
        CustomerId customerId = customer.getId();
        log.info("Received CustomerCreatedEvent for customer id: {} and username: {}",
                customerId.getValue(),
                customer.getUsername().getValue());


        try {
            CustomerAvroModel customerAvroModel =
                    customerMessagingDataMapper
                            .customerCreatedEventToCustomerRequestAvroModel(customerCreatedEvent);

            kafkaProducer.send(
                    customerServiceConfigData.getCustomerTopicName(),
                    customerAvroModel.getId(),
                    customerAvroModel,
                    getCallback( customerAvroModel.getId(), customerAvroModel));


            log.info("CustomerCreatedEvent sent to kafka for customer id: {} and username: {}",
                    customerAvroModel.getId(), customerAvroModel.getUsername());
        } catch (Exception e) {
            log.error("Error while sending CustomerCreatedEvent to kafka for customer id: {} and username: {}," +
                    " error: {}", customerId.getValue(), customer.getUsername().getValue(), e.getMessage());
        }
    }

    private BiConsumer<SendResult<String, CustomerAvroModel>, Throwable>
    getCallback(String topicName, CustomerAvroModel message) {
        return (result, ex)->{
            if (ex == null){
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Received new metadata. Topic: {}; Partition {}; Offset {}; Timestamp {}, at time {}",
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp(),
                        System.nanoTime());
            }else{
                log.error("Error while sending message {} to topic {}", message.toString(), topicName, ex);

            }
        };
    }
}
