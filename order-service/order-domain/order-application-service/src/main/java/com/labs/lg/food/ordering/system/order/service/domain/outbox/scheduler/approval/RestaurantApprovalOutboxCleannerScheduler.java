package com.labs.lg.food.ordering.system.order.service.domain.outbox.scheduler.approval;

import com.labs.lg.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.labs.lg.food.ordering.system.order.service.domain.saga.SagaStatus;
import com.lg5.spring.outbox.OutboxScheduler;
import com.lg5.spring.outbox.OutboxStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RestaurantApprovalOutboxCleannerScheduler implements OutboxScheduler {
    private final ApprovalOutboxHelper approvalOutboxHelper;

    public RestaurantApprovalOutboxCleannerScheduler(ApprovalOutboxHelper approvalOutboxHelper) {
        this.approvalOutboxHelper = approvalOutboxHelper;
    }

    @Override
    @Scheduled(cron = "@midnight")
    public void processOutboxMessage() {
        final Optional<List<OrderApprovalOutboxMessage>> outboxMessagesResponse =
                approvalOutboxHelper.getApprovalOutboxMessageByOutboxStatusAndSagaStatus(
                        OutboxStatus.COMPLETED,
                        SagaStatus.SUCCEEDED,
                        SagaStatus.FAILED,
                        SagaStatus.COMPENSATED);

        if (outboxMessagesResponse.isPresent()) {
            final List<OrderApprovalOutboxMessage> outboxMessages = outboxMessagesResponse.get();
            log.info("Received {} OrderApprovalOutboxMessage for clean-up. The payloads: {}",
                    outboxMessages.size(),
                    outboxMessages.stream().map(OrderApprovalOutboxMessage::getPayload)
                            .collect(Collectors.joining("\n")));
            approvalOutboxHelper.deleteApprovalOutboxMessageByOutboxStatusAndSagaStatus(
                    OutboxStatus.COMPLETED,
                    SagaStatus.SUCCEEDED,
                    SagaStatus.FAILED,
                    SagaStatus.COMPENSATED);
            log.info("{} OrderApprovalOutboxMessage deleted!", outboxMessages.size());
        }
    }
}
