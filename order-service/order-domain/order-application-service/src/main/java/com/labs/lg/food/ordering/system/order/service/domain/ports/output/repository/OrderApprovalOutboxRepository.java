package com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository;

import com.labs.lg.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.labs.lg.food.ordering.system.outbox.OutboxStatus;
import com.labs.lg.food.ordering.system.saga.SagaStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderApprovalOutboxRepository {
    OrderApprovalOutboxMessage save (OrderApprovalOutboxMessage orderApprovalOutboxMessage);
    Optional<List<OrderApprovalOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(String type,
                                                                                     OutboxStatus outboxStatus,
                                                                                     SagaStatus... sagaStatus);
    Optional<List<OrderApprovalOutboxMessage>> findByTypeAndSagaIdAndSagaStatus(String type,
                                                                               UUID sagaId,
                                                                               SagaStatus... sagaStatus);

    void deleteByTypeAndOutboxStatusAndSagaStatus(String type,
                                                  OutboxStatus outboxStatus,
                                                  SagaStatus... sagaStatus);
}
