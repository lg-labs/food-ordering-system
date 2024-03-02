package com.labs.lg.food.ordering.system.customer.service.domain.dto.creator;

import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
public record CreateCustomerResponse(@NotNull UUID customerId,
                                     @NotNull String message) {
}
