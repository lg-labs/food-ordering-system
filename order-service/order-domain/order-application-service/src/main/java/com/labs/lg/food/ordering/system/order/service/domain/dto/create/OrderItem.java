package com.labs.lg.food.ordering.system.order.service.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public record OrderItem(@NotNull UUID productId, @NotNull int quantity, @NotNull BigDecimal price,
                        @NotNull BigDecimal subtotal) {
}
