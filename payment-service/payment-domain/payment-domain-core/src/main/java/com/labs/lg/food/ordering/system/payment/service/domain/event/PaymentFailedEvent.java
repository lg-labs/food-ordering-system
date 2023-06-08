package com.labs.lg.food.ordering.system.payment.service.domain.event;

import com.labs.lg.food.ordering.system.payment.service.domain.entity.Payment;
import com.labs.lg.pentagon.common.domain.event.publisher.DomainEventPublisher;

import java.time.ZonedDateTime;
import java.util.List;

public class PaymentFailedEvent extends PaymentEvent {

  private final DomainEventPublisher<PaymentFailedEvent> paymentFailedEventDomainEventPublisher;

  public PaymentFailedEvent(
      Payment payment,
      ZonedDateTime createdAt,
      List<String> failureMessages,
      DomainEventPublisher<PaymentFailedEvent> paymentFailedEventDomainEventPublisher) {
    super(payment, createdAt, failureMessages);
    this.paymentFailedEventDomainEventPublisher = paymentFailedEventDomainEventPublisher;
  }

  @Override
  public void fire() {
    paymentFailedEventDomainEventPublisher.publish(this);
  }
}
