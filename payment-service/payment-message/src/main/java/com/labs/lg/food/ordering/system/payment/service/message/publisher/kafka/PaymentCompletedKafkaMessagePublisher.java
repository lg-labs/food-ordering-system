package com.labs.lg.food.ordering.system.payment.service.message.publisher.kafka;

import com.labs.lg.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.labs.lg.food.ordering.system.kafka.producer.KafkaMessageHelper;
import com.labs.lg.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.labs.lg.food.ordering.system.payment.service.domain.config.PaymentServiceConfigData;
import com.labs.lg.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent;
import com.labs.lg.food.ordering.system.payment.service.domain.ports.output.message.publisher.PaymentCompletedMessagePublisher;
import com.labs.lg.food.ordering.system.payment.service.message.mapper.PaymentMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentCompletedKafkaMessagePublisher implements PaymentCompletedMessagePublisher {
  private final PaymentMessagingDataMapper paymentMessagingDataMapper;
  private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;
  private final PaymentServiceConfigData paymentServiceConfigData;
  private final KafkaMessageHelper kafkaMessageHelper;

  public PaymentCompletedKafkaMessagePublisher(PaymentMessagingDataMapper paymentMessagingDataMapper,
                                               KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer,
                                               PaymentServiceConfigData paymentServiceConfigData,
                                               KafkaMessageHelper kafkaMessageHelper) {
    this.paymentMessagingDataMapper = paymentMessagingDataMapper;
    this.kafkaProducer = kafkaProducer;
    this.paymentServiceConfigData = paymentServiceConfigData;
    this.kafkaMessageHelper = kafkaMessageHelper;
  }

  @Override
  public void publish(PaymentCompletedEvent domainEvent) {
    String orderId = domainEvent.getPayment().getOrderId().getValue().toString();

    log.info("Received PaymentCompletedEvent for order id: {}", orderId);

    try {
      PaymentResponseAvroModel paymentResponseAvroModel = paymentMessagingDataMapper
          .paymentCompletedEventToPaymentResponseAvroModel(domainEvent);

      kafkaProducer.send(paymentServiceConfigData.getPaymentResponseTopicName(),
          orderId, paymentResponseAvroModel,
          kafkaMessageHelper.getKafkaCallback(
              paymentServiceConfigData.getPaymentResponseTopicName(),
              paymentResponseAvroModel,
              orderId,
              "PaymentResponseAvroModel"));

      log.info("PaymentResponseAvroModel sent to Kafka for order id: {}", orderId);
    } catch (Exception e) {
      log.error("Error while sending PaymentResponseAvroModel message"
          + "to kafka with order id: {}, error: {}", orderId, e.getMessage()
      );
    }
  }
}
