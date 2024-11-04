package com.labs.lg.food.ordering.system.order.service.domain.data;

import com.labs.lg.food.ordering.system.order.service.domain.boot.Bootstrap;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Order;
import com.labs.lg.food.ordering.system.order.service.domain.entity.OrderItem;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Product;
import com.labs.lg.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.CustomerId;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.OrderId;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.OrderStatus;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.ProductId;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.RestaurantId;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.TrackingId;
import com.labs.lg.pentagon.common.domain.valueobject.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrdersIT extends Bootstrap {

    @Autowired
    private OrderRepository repository;

    @Test
    void it_should_create_a_order_as_mock() {
        // given
        final TrackingId trackingId = new TrackingId(UUID.randomUUID());
        final Order order = Order.builder()
                .orderId(new OrderId(UUID.randomUUID()))
                .customerId(new CustomerId(UUID.randomUUID()))
                .restaurantId(new RestaurantId(UUID.randomUUID()))
                .trackingId(trackingId)
                .orderStatus(OrderStatus.APPROVED)
                .deliveryAddress(new StreetAddress(UUID.randomUUID(), "a", "b", "c"))
                .failureMessages(List.of("lorem1", "lorem2", "lorem3", "lorem4", "lorem5"))
                .price(new Money(new BigDecimal(1)))
                .items(List.of(OrderItem.builder()
                                .orderItemId(new OrderItemId(1L))
                                .product(new Product(new ProductId(UUID.randomUUID())))
                                .quantity(1)
                                .price(new Money(new BigDecimal(1)))
                                .subtotal(new Money(new BigDecimal(1)))
                        .build()))
                .build();

        // when
        repository.save(order);

        // then
        final Optional<Order> orderOptional = repository.findByTrackingId(trackingId);

        assertTrue(orderOptional.isPresent());
        assertEquals(trackingId, orderOptional.get().getTrackingId());

    }
}
