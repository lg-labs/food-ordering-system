package com.labs.lg.food.ordering.system.order.service.domain.outbox.scheduler.payment;

import com.labs.lg.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.labs.lg.food.ordering.system.outbox.OutboxScheduler;
import com.labs.lg.food.ordering.system.outbox.OutboxStatus;
import com.labs.lg.food.ordering.system.saga.SagaStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PaymentOutBoxCleannerScheduler implements OutboxScheduler {
    private final PaymentOutboxHelper paymentOutboxHelper;

    public PaymentOutBoxCleannerScheduler(PaymentOutboxHelper paymentOutboxHelper) {
        this.paymentOutboxHelper = paymentOutboxHelper;
    }

    @Override
    @Transactional
    @Scheduled(cron = "@midnight")
    public void processOutboxMessage() {
        final Optional<List<OrderPaymentOutboxMessage>> outboxMessagesResponse = paymentOutboxHelper
                .getPaymentOutboxMessageByOutboxStatusAndSagaStatus(
                        OutboxStatus.COMPLETED,
                        SagaStatus.SUCCEEDED,
                        SagaStatus.FAILED,
                        SagaStatus.COMPENSATED);

        if (outboxMessagesResponse.isPresent()) {
            final List<OrderPaymentOutboxMessage> outboxMessages = outboxMessagesResponse.get();
            log.info("Received {} OrderPaymentOutboxMessage for clean-up. The Payloads: {}",
                    outboxMessages.size(),
                    outboxMessages.stream().map(OrderPaymentOutboxMessage::getPayload).collect(Collectors.joining("\n")));

            // TODO: No remove data, Then, insert in other table as archived and later analyzed all system.
            // For this case, only remove data. In prod, is not recommended
            paymentOutboxHelper.deletePaymentOutboxMessageByOutboxStatusAndSagaStatus(OutboxStatus.COMPLETED,
                    SagaStatus.SUCCEEDED,
                    SagaStatus.FAILED,
                    SagaStatus.COMPENSATED);

            log.info("{} OrderPaymentOutboxMessage deleted!", outboxMessages.size());
        }
    }
}
