package com.labs.lg.food.ordering.system.order.service.domain.entity;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class OrderItemMother {

    public static Stream<Arguments> provideOrderItem() {
        return Stream.of(Arguments.of(
                OrderItem.Builder.builder().build()
                )
        );
    }
}
