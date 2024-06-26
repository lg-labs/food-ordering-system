package com.labs.lg.food.ordering.system.payment.service.domain.ports.output.repository;

import com.labs.lg.food.ordering.system.payment.service.domain.valueobject.PaymentStatus;
import com.labs.lg.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import com.lg5.spring.outbox.OutboxStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderOutboxRepository {
    OrderOutboxMessage save(OrderOutboxMessage orderOutboxMessage);

    Optional<List<OrderOutboxMessage>> findByTypeAndOutboxStatus(String type, OutboxStatus status);

    Optional<OrderOutboxMessage> findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(String type,
                                                                                    UUID sagaId,
                                                                                    PaymentStatus paymentStatus,
                                                                                    OutboxStatus outboxStatus);

    void deleteByTypeAndOutboxStatus(String type, OutboxStatus status);
}
