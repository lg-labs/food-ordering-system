package com.labs.lg.food.ordering.system.payment.service.message.publisher.kafka;

import com.labs.lg.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.labs.lg.food.ordering.system.kafka.producer.KafkaMessageHelper;
import com.labs.lg.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.labs.lg.food.ordering.system.payment.service.domain.config.PaymentServiceConfigData;
import com.labs.lg.food.ordering.system.payment.service.domain.event.PaymentFailedEvent;
import com.labs.lg.food.ordering.system.payment.service.domain.ports.output.message.publisher.PaymentFailedMessagePublisher;
import com.labs.lg.food.ordering.system.payment.service.message.mapper.PaymentMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class PaymentFailedKafkaMessagePublisher implements PaymentFailedMessagePublisher {
  private final PaymentMessagingDataMapper paymentMessagingDataMapper;
  private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;
  private final PaymentServiceConfigData paymentServiceConfigData;
  private final KafkaMessageHelper kafkaMessageHelper;

  public PaymentFailedKafkaMessagePublisher(PaymentMessagingDataMapper paymentMessagingDataMapper,
                                            KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer,
                                            PaymentServiceConfigData paymentServiceConfigData, KafkaMessageHelper kafkaMessageHelper) {
    this.paymentMessagingDataMapper = paymentMessagingDataMapper;
    this.kafkaProducer = kafkaProducer;
    this.paymentServiceConfigData = paymentServiceConfigData;
    this.kafkaMessageHelper = kafkaMessageHelper;
  }

  @Override
  public void publish(PaymentFailedEvent domainEvent) {
    String orderId = domainEvent.getPayment().getOrderId().getValue().toString();

    log.info("Received PaymentFailedEvent for order id: {}", orderId);

    PaymentResponseAvroModel paymentResponseAvroModel = paymentMessagingDataMapper
        .paymentFailedEventToPaymentResponseAvroModel(domainEvent);

    kafkaProducer.send(paymentServiceConfigData.getPaymentResponseTopicName(),
        orderId,
        paymentResponseAvroModel,
        kafkaMessageHelper.getKafkaCallback(
            paymentServiceConfigData.getPaymentResponseTopicName(),
            paymentResponseAvroModel,
            orderId,
            "paymentResponseAvroModel"));
  }
}
