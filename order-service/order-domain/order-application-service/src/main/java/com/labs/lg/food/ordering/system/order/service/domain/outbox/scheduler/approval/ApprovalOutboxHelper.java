package com.labs.lg.food.ordering.system.order.service.domain.outbox.scheduler.approval;

import com.labs.lg.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.labs.lg.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository.OrderApprovalOutboxRepository;
import com.labs.lg.food.ordering.system.outbox.OutboxStatus;
import com.labs.lg.food.ordering.system.saga.SagaStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.labs.lg.food.ordering.system.saga.order.SagaConstants.ORDER_SAGA_NAME;

@Slf4j
@Component
public class ApprovalOutboxHelper {

    private final OrderApprovalOutboxRepository orderApprovalOutboxRepository;

    public ApprovalOutboxHelper(OrderApprovalOutboxRepository orderApprovalOutboxRepository) {
        this.orderApprovalOutboxRepository = orderApprovalOutboxRepository;

    }

    @Transactional(readOnly = true)
    public Optional<List<OrderApprovalOutboxMessage>>
    getApprovalOutboxMessageByOutboxStatusAndSagaStatus(OutboxStatus outboxStatus,
                                                                    SagaStatus... sagaStatus){
        return orderApprovalOutboxRepository.findByTypeAndOutboxStatusAndSagaStatus(ORDER_SAGA_NAME, outboxStatus, sagaStatus);

    }

    @Transactional(readOnly = true)
    public Optional<List<OrderApprovalOutboxMessage>>
    getApprovalOutboxMessageBySagaIdAndSagaStatus(UUID sagaId, SagaStatus... sagaStatus){
        return orderApprovalOutboxRepository.findByTypeAndSagaIdAndSagaStatus(ORDER_SAGA_NAME, sagaId , sagaStatus);

    }

    @Transactional
    public void save(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        UUID outboxMessageId = orderApprovalOutboxMessage.getId();
        OrderApprovalOutboxMessage response = orderApprovalOutboxRepository.save(orderApprovalOutboxMessage);
        if (response == null) {

            log.error("Could not save OrderApprovalOutboxMessage with id: {}", outboxMessageId);
            throw new OrderDomainException("Could not save OrderApprovalOutboxMessage with id: " + outboxMessageId);
        }
        log.info("OrderApprovalOutboxMessage saved with outbox id: {}", outboxMessageId);
    }

    @Transactional
    public void deleteApprovalOutboxMessageByOutboxStatusAndSagaStatus(OutboxStatus outboxStatus,
                                                                       SagaStatus... sagaStatus){
        orderApprovalOutboxRepository.deleteByTypeAndOutboxStatusAndSagaStatus(ORDER_SAGA_NAME, outboxStatus, sagaStatus);
    }


}
