package com.labs.lg.food.ordering.system.order.service.domain.dto.create;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;



public record CreateOrderCommand(@NotNull UUID customerId, @NotNull UUID restaurantId, @NotNull BigDecimal price,
                                 @NotNull List<OrderItem> items, @NotNull OrderAddress address) {

}
