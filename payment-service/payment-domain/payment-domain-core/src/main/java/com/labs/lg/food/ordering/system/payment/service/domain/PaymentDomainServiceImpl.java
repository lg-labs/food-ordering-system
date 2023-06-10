package com.labs.lg.food.ordering.system.payment.service.domain;

import com.labs.lg.food.ordering.system.domain.valueobject.PaymentStatus;
import com.labs.lg.food.ordering.system.payment.service.domain.entity.CreditEntry;
import com.labs.lg.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.labs.lg.food.ordering.system.payment.service.domain.entity.Payment;
import com.labs.lg.food.ordering.system.payment.service.domain.event.PaymentCancelledEvent;
import com.labs.lg.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent;
import com.labs.lg.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.labs.lg.food.ordering.system.payment.service.domain.event.PaymentFailedEvent;
import com.labs.lg.food.ordering.system.payment.service.domain.valueobject.CreditHistoryId;
import com.labs.lg.food.ordering.system.payment.service.domain.valueobject.TransactionType;
import com.labs.lg.pentagon.common.domain.valueobject.Money;
import com.labs.lg.pentagon.common.domain.event.publisher.DomainEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static com.labs.lg.pentagon.common.domain.DomainConstants.UTC;

/**
 *
 *
 * <h1>Bounded Context</h1>
 *
 * <h2>Payment Service</h2>
 */
public class PaymentDomainServiceImpl implements PaymentDomainService {

  private static final Logger log = LoggerFactory.getLogger(PaymentDomainServiceImpl.class);

  @Override
  public PaymentEvent validateAndInitiatePayment(
      Payment payment,
      CreditEntry creditEntry,
      List<CreditHistory> creditHistories,
      List<String> failureMessages,
      DomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventDomainEventPublisher,
      DomainEventPublisher<PaymentFailedEvent> paymentFailedEventDomainEventPublisher) {

    payment.validatePayment(failureMessages);
    payment.initializePayment();
    validateCreditEntry(payment, creditEntry, failureMessages);
    subtractCreditEntry(payment, creditEntry);
    updateCreditHistory(payment, creditHistories, TransactionType.DEBIT);
    validateCreditHistory(creditEntry, creditHistories, failureMessages);

    if (failureMessages.isEmpty()) {
      log.info("Payment initiate for order id: {}  ", payment.getOrderId().getValue());
      payment.updateStatus(PaymentStatus.COMPLETED);
      return new PaymentCompletedEvent(
          payment, ZonedDateTime.now(ZoneId.of(UTC)), paymentCompletedEventDomainEventPublisher);
    }
    log.info("Payment initiation is failed for order id: {}", payment.getOrderId().getValue());
    payment.updateStatus(PaymentStatus.FAILED);
    return new PaymentFailedEvent(
        payment, ZonedDateTime.now(ZoneId.of(UTC)), failureMessages, paymentFailedEventDomainEventPublisher);
  }

  @Override
  public PaymentEvent validateAndCancelledPayment(
      Payment payment,
      CreditEntry creditEntry,
      List<CreditHistory> creditHistories,
      List<String> failureMessages,
      DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventDomainEventPublisher,
      DomainEventPublisher<PaymentFailedEvent> paymentFailedEventDomainEventPublisher) {
    payment.validatePayment(failureMessages);
    addCreditEntry(payment, creditEntry);
    updateCreditHistory(payment, creditHistories, TransactionType.CREDIT);
    if (failureMessages.isEmpty()) {
      log.info("Payment is cancelled for order id: {}", payment.getOrderId().getValue());
      payment.updateStatus(PaymentStatus.CANCELLED);
      return new PaymentCancelledEvent(
          payment, ZonedDateTime.now(ZoneId.of(UTC)), paymentCancelledEventDomainEventPublisher);
    } else {
      log.info("Payment cancellation is failed for order id: {}", payment.getOrderId().getValue());
      payment.updateStatus(PaymentStatus.FAILED);
      return new PaymentFailedEvent(
          payment, ZonedDateTime.now(ZoneId.of(UTC)), failureMessages, paymentFailedEventDomainEventPublisher);
    }
  }

  private void addCreditEntry(Payment payment, CreditEntry creditEntry) {
    creditEntry.addCreditAmount(payment.getPrice());
  }

  private void validateCreditHistory(
      CreditEntry creditEntry, List<CreditHistory> creditHistories, List<String> failureMessages) {
    Money totalCreditHistory = getTotalCreditHistory(creditHistories, TransactionType.CREDIT);

    Money totalDebitHistory = getTotalCreditHistory(creditHistories, TransactionType.DEBIT);

    if (totalDebitHistory.isGreaterThan(totalCreditHistory)) {
      log.error(
          "Customer with id: {} doesn't have enough credit according to credit history",
          creditEntry.getCustomerId().getValue());
      failureMessages.add(
          "Customer with id: "
              + creditEntry.getCustomerId().getValue()
              + " doesn't have enough credit according to credit history");
    }

    if (!creditEntry.getTotalCreditAmount().equals(totalCreditHistory.subtract(totalDebitHistory))) {
      log.error(
          "Credit history total is not equal to current credit for customer id: {}!",
          creditEntry.getCustomerId().getValue());

      failureMessages.add(
          "Credit history total is not equal to current credit for customer id: "
              + creditEntry.getCustomerId().getValue()
              + "!");
    }
  }

  private static Money getTotalCreditHistory(List<CreditHistory> creditHistories, TransactionType credit) {
    return creditHistories.stream()
        .filter(creditHistory -> credit == creditHistory.getTransactionType())
        .map(CreditHistory::getAmount)
        .reduce(Money.ZERO, Money::add);
  }

  private void updateCreditHistory(
      Payment payment, List<CreditHistory> creditHistories, TransactionType transactionType) {
    creditHistories.add(
        CreditHistory.builder()
            .creditHistoryId(new CreditHistoryId(UUID.randomUUID()))
            .customerId(payment.getCustomerId())
            .amount(payment.getPrice())
            .transactionType(transactionType)
            .build());
  }

  private void subtractCreditEntry(Payment payment, CreditEntry creditEntry) {
    creditEntry.subtractCreditAmount(payment.getPrice());
  }

  private void validateCreditEntry(Payment payment, CreditEntry creditEntry, List<String> failureMessages) {
    if (payment.getPrice().isGreaterThan(creditEntry.getTotalCreditAmount())) {
      log.error("Customer with id: {} doesn't have enough credit for payment!", payment.getOrderId().getValue());
      failureMessages.add(
          "Customer with id= " + payment.getOrderId().getValue() + " doesn't have enough credit for payment!");
    }
  }
}
