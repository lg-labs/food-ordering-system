package com.labs.lg.food.ordering.system.order.service.domain.entity;

import com.labs.lg.food.ordering.system.domain.valueobject.CustomerId;
import com.labs.lg.food.ordering.system.domain.valueobject.RestaurantId;
import com.labs.lg.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.labs.lg.pentagon.common.domain.valueobject.Money;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.labs.lg.food.ordering.system.order.service.domain.entity.OrderItemMother.provideOrderItems;

public class OrderMother {

    private static Order provideOrder() {
        return Order.builder()
                .customerId(new CustomerId(UUID.randomUUID()))
                .restaurantId(new RestaurantId(UUID.randomUUID()))
                .deliveryAddress(getStreetAddress())
                .price(new Money(BigDecimal.ONE))
                .items(provideOrderItems())
                .failureMessages(new ArrayList<>(List.of("The order has messages")))
                .build();
    }

    private static StreetAddress getStreetAddress() {
        return new StreetAddress(UUID.randomUUID(), "street_1", "1000AB", "Amsterdam");
    }

    static final class OrderArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(Arguments.of(provideOrder()));
        }
    }
}


