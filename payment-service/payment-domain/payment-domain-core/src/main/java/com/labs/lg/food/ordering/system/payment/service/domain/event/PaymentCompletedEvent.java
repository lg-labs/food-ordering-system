package com.labs.lg.food.ordering.system.payment.service.domain.event;

import com.labs.lg.food.ordering.system.payment.service.domain.entity.Payment;
import com.labs.lg.pentagon.common.domain.event.publisher.DomainEventPublisher;

import java.time.ZonedDateTime;
import java.util.Collections;

public class PaymentCompletedEvent extends PaymentEvent {

  private final DomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventDomainEventPublisher;

  public PaymentCompletedEvent(
      Payment payment,
      ZonedDateTime createdAt,
      DomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventDomainEventPublisher) {
    super(payment, createdAt, Collections.emptyList());
    this.paymentCompletedEventDomainEventPublisher = paymentCompletedEventDomainEventPublisher;
  }

  @Override
  public void fire() {
    paymentCompletedEventDomainEventPublisher.publish(this);
  }
}
