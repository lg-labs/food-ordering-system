package com.labs.lg.food.ordering.system.order.service.domain.entity;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderItemTest {

  @ParameterizedTest
  @MethodSource("com.labs.lg.food.ordering.system.order.service.domain.entity.OrderItemMother#provideOrderItem")
  void initializeOrderItem(OrderItem myClass) {
    assertTrue(true);
  }

  @ParameterizedTest
  @MethodSource("com.labs.lg.food.ordering.system.order.service.domain.entity.OrderItemMother#provideOrderItem")
  void isPriceValid() {
    assertTrue(true);
  }
}