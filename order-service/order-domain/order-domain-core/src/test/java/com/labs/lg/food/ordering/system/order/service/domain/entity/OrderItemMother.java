package com.labs.lg.food.ordering.system.order.service.domain.entity;

import com.labs.lg.food.ordering.system.domain.valueobject.ProductId;
import com.labs.lg.pentagon.common.domain.valueobject.Money;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class OrderItemMother {

    public static List<OrderItem> provideOrderItems() {
        return new ArrayList<>(List.of(OrderItem.builder()
                .price(new Money(new BigDecimal("1.00")))
                .product(getProduct())
                .quantity(1)
                .subtotal(new Money(new BigDecimal("1.00")))
                .build()));
    }

    private static Product getProduct() {
        return new Product(new ProductId(UUID.randomUUID()), "Product1", new Money(new BigDecimal("1.00")));
    }

    public static final class OrderItemArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(Arguments.of(provideOrderItems()));
        }
    }
}


