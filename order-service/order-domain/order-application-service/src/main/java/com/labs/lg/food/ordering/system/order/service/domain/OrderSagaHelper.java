package com.labs.lg.food.ordering.system.order.service.domain;


import com.labs.lg.food.ordering.system.order.service.domain.entity.Order;
import com.labs.lg.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.labs.lg.food.ordering.system.order.service.domain.saga.SagaStatus;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.OrderId;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderSagaHelper {
    private final OrderRepository repository;

    public OrderSagaHelper(OrderRepository repository) {
        this.repository = repository;
    }

    public Order findOrder(String orderId) {
        final Optional<Order> orderResponse = repository.findById(new OrderId(UUID.fromString(orderId)));
        if (orderResponse.isEmpty()) {
            log.error("Order with id: {} could not be found!", orderId);
            throw new OrderNotFoundException("Order with id: " + orderId + " could not be found!");
        }
        return orderResponse.get();
    }

    public void saveOrder(Order order) {
        repository.save(order);
    }

    SagaStatus orderStatusToSagaStatus(OrderStatus orderStatus) {
        return switch (orderStatus) {
            case PAID -> SagaStatus.PROCESSING;
            case APPROVED -> SagaStatus.SUCCEEDED;
            case CANCELLING -> SagaStatus.COMPENSATING;
            case CANCELLED -> SagaStatus.COMPENSATED;
            //default when OrderStatus is PENDING
            default -> SagaStatus.STARTED;
        };
    }
}
