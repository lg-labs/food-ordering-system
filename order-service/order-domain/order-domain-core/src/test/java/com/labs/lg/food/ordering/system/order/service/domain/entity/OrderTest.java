package com.labs.lg.food.ordering.system.order.service.domain.entity;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderTest {
    @ParameterizedTest
    @ArgumentsSource(OrderMother.OrderArgumentsProvider.class)
    void it_should_update_failure_messages_when_order_has_messages_and_receive_a_message_empty(Order order) {
        // given
        order.initializeOrder();
        List<String> failureMessages = new ArrayList<>();
        assertNotNull(order.getFailureMessages());
        // when
        order.cancel(failureMessages);

        // then
        assertEquals(1, order.getFailureMessages().size());
    }
}
