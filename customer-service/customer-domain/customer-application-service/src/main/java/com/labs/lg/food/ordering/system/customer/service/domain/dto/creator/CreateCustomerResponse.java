package com.labs.lg.food.ordering.system.customer.service.domain.dto.creator;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateCustomerResponse(@NotNull UUID customerId,
                                     @NotNull String message) {
}
