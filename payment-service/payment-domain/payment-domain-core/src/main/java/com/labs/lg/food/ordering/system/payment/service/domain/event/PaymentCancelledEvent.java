package com.labs.lg.food.ordering.system.payment.service.domain.event;

import com.labs.lg.food.ordering.system.payment.service.domain.entity.Payment;
import com.labs.lg.pentagon.common.domain.event.publisher.DomainEventPublisher;

import java.time.ZonedDateTime;
import java.util.Collections;

public class PaymentCancelledEvent extends PaymentEvent {

  private final DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventDomainEventPublisher;

  public PaymentCancelledEvent(
      Payment payment,
      ZonedDateTime createdAt,
      DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventDomainEventPublisher) {
    super(payment, createdAt, Collections.emptyList());
    this.paymentCancelledEventDomainEventPublisher = paymentCancelledEventDomainEventPublisher;
  }

  @Override
  public void fire() {
    paymentCancelledEventDomainEventPublisher.publish(this);
  }
}
