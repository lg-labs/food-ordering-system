package com.labs.lg.food.ordering.system.order.service.domain.entity;

import com.labs.lg.food.ordering.system.domain.valueobject.OrderId;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderItemTest {

    @ParameterizedTest
    @ArgumentsSource(OrderItemMother.OrderItemArgumentsProvider.class)
    void initializeOrderItem(List<OrderItem> orderItems) {
        OrderItem orderItem = orderItems.stream().findFirst().orElse(OrderItem.builder().build());
        OrderId orderId = new OrderId(UUID.randomUUID());
        OrderItemId orderItemId = new OrderItemId(1L);
        // when
        orderItem.initializeOrderItem(orderId, orderItemId);
        // then
        assertNotNull(orderItems);
    }

    @ParameterizedTest
    @ArgumentsSource(OrderItemMother.OrderItemArgumentsProvider.class)
    void isPriceValid(List<OrderItem> orderItems) {
        OrderItem orderItem = orderItems.stream().findFirst().orElse(OrderItem.builder().build());
        OrderId orderId = new OrderId(UUID.randomUUID());
        OrderItemId orderItemId = new OrderItemId(1L);
        // when
        orderItem.initializeOrderItem(orderId, orderItemId);
        boolean priceValidReceived = orderItem.isPriceValid();
        assertTrue(priceValidReceived);
    }
}