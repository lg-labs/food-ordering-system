package com.labs.lg.food.ordering.system.payment.service.domain;

import com.labs.lg.food.ordering.system.payment.service.domain.entity.CreditEntry;
import com.labs.lg.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.labs.lg.food.ordering.system.payment.service.domain.entity.Payment;
import com.labs.lg.food.ordering.system.payment.service.domain.event.PaymentCancelledEvent;
import com.labs.lg.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent;
import com.labs.lg.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.labs.lg.food.ordering.system.payment.service.domain.event.PaymentFailedEvent;
import com.labs.lg.pentagon.common.domain.event.publisher.DomainEventPublisher;

import java.util.List;

public interface PaymentDomainService {
  PaymentEvent validateAndInitiatePayment(
      Payment payment,
      CreditEntry creditEntry,
      List<CreditHistory> creditHistories,
      List<String> failureMessages,
      DomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventDomainEventPublisher,
      DomainEventPublisher<PaymentFailedEvent> paymentFailedEventDomainEventPublisher);

  PaymentEvent validateAndCancelledPayment(
      Payment payment,
      CreditEntry creditEntry,
      List<CreditHistory> creditHistories,
      List<String> failureMessages,
      DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventDomainEventPublisher,
      DomainEventPublisher<PaymentFailedEvent> paymentFailedEventDomainEventPublisher);
}
