package com.labs.lg.food.ordering.system.payment.service.domain;

import com.labs.lg.food.ordering.system.domain.valueobject.CustomerId;
import com.labs.lg.food.ordering.system.domain.valueobject.PaymentStatus;
import com.labs.lg.food.ordering.system.outbox.OutboxStatus;
import com.labs.lg.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.labs.lg.food.ordering.system.payment.service.domain.entity.CreditEntry;
import com.labs.lg.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.labs.lg.food.ordering.system.payment.service.domain.entity.Payment;
import com.labs.lg.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.labs.lg.food.ordering.system.payment.service.domain.exception.PaymentApplicationServiceException;
import com.labs.lg.food.ordering.system.payment.service.domain.exception.PaymentNotFoundException;
import com.labs.lg.food.ordering.system.payment.service.domain.mapper.PaymentDataMapper;
import com.labs.lg.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import com.labs.lg.food.ordering.system.payment.service.domain.outbox.scheduler.OrderOutboxHelper;
import com.labs.lg.food.ordering.system.payment.service.domain.ports.output.message.publisher.PaymentResponseMessagePublisher;
import com.labs.lg.food.ordering.system.payment.service.domain.ports.output.repository.CreditEntryRepository;
import com.labs.lg.food.ordering.system.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import com.labs.lg.food.ordering.system.payment.service.domain.ports.output.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class PaymentRequestHelper {

    private final PaymentDomainService paymentDomainService;
    private final PaymentDataMapper paymentDataMapper;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final OrderOutboxHelper orderOutboxHelper;
    private final PaymentResponseMessagePublisher paymentResponseMessagePublisher;

    public PaymentRequestHelper(PaymentDomainService paymentDomainService,
                                PaymentDataMapper paymentDataMapper,
                                PaymentRepository paymentRepository,
                                CreditEntryRepository creditEntryRepository,
                                CreditHistoryRepository creditHistoryRepository,
                                OrderOutboxHelper orderOutboxHelper,
                                PaymentResponseMessagePublisher paymentResponseMessagePublisher) {
        this.paymentDomainService = paymentDomainService;
        this.paymentDataMapper = paymentDataMapper;
        this.paymentRepository = paymentRepository;
        this.creditEntryRepository = creditEntryRepository;
        this.creditHistoryRepository = creditHistoryRepository;
        this.orderOutboxHelper = orderOutboxHelper;
        this.paymentResponseMessagePublisher = paymentResponseMessagePublisher;
    }

    @Transactional
    public void persistPayment(PaymentRequest paymentRequest) {
        if (publishIfOutboxMessageProcessedForPayment(paymentRequest, PaymentStatus.COMPLETED)) {
            log.info("An outbox message with saga id: {} is already saved to database!",
                    paymentRequest.getSagaId());
            return;
        }

        log.info("Received payment complete event for order id: {}", paymentRequest.getOrderId());
        final Payment payment = paymentDataMapper.paymentRequestModelToPayment(paymentRequest);
        final CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        final List<CreditHistory> creditHistories = getCreditHistory(
                payment.getCustomerId()
        );
        final List<String> failureMessages = new ArrayList<>();
        final PaymentEvent paymentEvent = paymentDomainService.validateAndInitiatePayment(
                payment,
                creditEntry,
                creditHistories,
                failureMessages
        );
        persistDbObjects(payment, creditEntry, creditHistories, failureMessages);

        orderOutboxHelper.saveOrderOutboxMessage(paymentDataMapper.paymentEventToOrderEventPayload(paymentEvent),
                paymentEvent.getPayment().getPaymentStatus(),
                OutboxStatus.STARTED,
                UUID.fromString(paymentRequest.getSagaId()));
    }

    @Transactional
    public void persistCancelPayment(PaymentRequest paymentRequest) {
        if (publishIfOutboxMessageProcessedForPayment(paymentRequest, PaymentStatus.CANCELLED)) {
            log.info("An outbox message with saga id: {} is already saved to database!",
                    paymentRequest.getSagaId());
            return;
        }

        log.info("Received payment rollback event to order id: {} ", paymentRequest.getOrderId());
        final Optional<Payment> paymentResponse = paymentRepository.findByOrderId(
                UUID.fromString(paymentRequest.getOrderId())
        );
        if (paymentResponse.isEmpty()) {
            log.error(
                    "Payment with order id: {} could not be found!",
                    paymentRequest.getOrderId()
            );
            throw new PaymentNotFoundException("Payment with order id: "
                    + paymentRequest.getOrderId() + " could not be found!");
        }
        final Payment payment = paymentResponse.get();
        final CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        final List<CreditHistory> creditHistories = getCreditHistory(payment.getCustomerId());
        final List<String> failureMessages = new ArrayList<>();
        final PaymentEvent paymentEvent = paymentDomainService.validateAndCancelledPayment(payment,
                creditEntry,
                creditHistories,
                failureMessages);
        persistDbObjects(payment, creditEntry, creditHistories, failureMessages);

        orderOutboxHelper.saveOrderOutboxMessage(paymentDataMapper.paymentEventToOrderEventPayload(paymentEvent),
                paymentEvent.getPayment().getPaymentStatus(),
                OutboxStatus.STARTED,
                UUID.fromString(paymentRequest.getSagaId()));
    }

    private CreditEntry getCreditEntry(CustomerId customerId) {
        final Optional<CreditEntry> creditEntry = creditEntryRepository.findByCustomerId(
                customerId
        );
        if (creditEntry.isEmpty()) {
            log.error(
                    "Could not find credit entry for customer: {}",
                    customerId.getValue()
            );
            throw new PaymentApplicationServiceException(
                    "Could not find credit entry for customer: +" + customerId.getValue()
            );
        }
        return creditEntry.get();
    }

    private List<CreditHistory> getCreditHistory(CustomerId customerId) {
        final Optional<List<CreditHistory>> creditHistories =
                creditHistoryRepository.findByCustomerId(customerId);
        if (creditHistories.isEmpty()) {
            log.error(
                    "Could not find credit history for customer: {}",
                    customerId.getValue()
            );
            throw new PaymentApplicationServiceException(
                    "Could not find credit history for customer: +" + customerId.getValue()
            );
        }
        return creditHistories.get();
    }

    private void persistDbObjects(Payment payment, CreditEntry creditEntry,
                                  List<CreditHistory> creditHistories, List<String> failureMessages) {
        paymentRepository.save(payment);
        if (failureMessages.isEmpty()) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
        }
    }

    private boolean publishIfOutboxMessageProcessedForPayment(PaymentRequest paymentRequest,
                                                              PaymentStatus paymentStatus) {
        final Optional<OrderOutboxMessage> orderOutboxMessage =
                orderOutboxHelper.getCompletedOrderOutboxMessageBySagaIdAndPaymentStatus(
                        UUID.fromString(paymentRequest.getSagaId()),
                        paymentStatus);
        if (orderOutboxMessage.isPresent()) {
            paymentResponseMessagePublisher.publish(orderOutboxMessage.get(), orderOutboxHelper::updateOutboxMessage);
            return true;
        }
        return false;
    }
}
