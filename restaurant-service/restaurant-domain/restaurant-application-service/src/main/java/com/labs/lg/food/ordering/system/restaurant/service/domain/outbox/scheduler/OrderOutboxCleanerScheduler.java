package com.labs.lg.food.ordering.system.restaurant.service.domain.outbox.scheduler;

import com.labs.lg.food.ordering.system.restaurant.service.domain.outbox.model.OrderOutboxMessage;
import com.lg5.spring.outbox.OutboxScheduler;
import com.lg5.spring.outbox.OutboxStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class OrderOutboxCleanerScheduler implements OutboxScheduler {

    private final OrderOutboxHelper orderOutboxHelper;

    public OrderOutboxCleanerScheduler(OrderOutboxHelper orderOutboxHelper) {
        this.orderOutboxHelper = orderOutboxHelper;
    }

    @Transactional
    @Scheduled(cron = "@midnight")
    @Override
    public void processOutboxMessage() {
        final Optional<List<OrderOutboxMessage>> outboxMessagesResponse =
                orderOutboxHelper.getOrderOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);
        if (outboxMessagesResponse.isPresent() && !outboxMessagesResponse.get().isEmpty()) {
            final List<OrderOutboxMessage> outboxMessages = outboxMessagesResponse.get();
            log.info("Received {} OrderOutboxMessage for clean-up!", outboxMessages.size());
            orderOutboxHelper.deleteOrderOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);
            log.info("Deleted {} OrderOutboxMessage!", outboxMessages.size());
        }
    }
}
