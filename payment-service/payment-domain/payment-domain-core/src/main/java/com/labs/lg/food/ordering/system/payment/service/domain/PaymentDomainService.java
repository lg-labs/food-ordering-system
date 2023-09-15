package com.labs.lg.food.ordering.system.payment.service.domain;

import com.labs.lg.food.ordering.system.payment.service.domain.entity.CreditEntry;
import com.labs.lg.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.labs.lg.food.ordering.system.payment.service.domain.entity.Payment;
import com.labs.lg.food.ordering.system.payment.service.domain.event.PaymentEvent;

import java.util.List;

public interface PaymentDomainService {
    PaymentEvent validateAndInitiatePayment(
            Payment payment,
            CreditEntry creditEntry,
            List<CreditHistory> creditHistories,
            List<String> failureMessages);

    PaymentEvent validateAndCancelledPayment(
            Payment payment,
            CreditEntry creditEntry,
            List<CreditHistory> creditHistories,
            List<String> failureMessages);
}
