package com.labs.lg.food.ordering.system.customer.service.domain.dto.creator;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;


public record CreateCustomerCommand(@NotNull UUID customerId,
                                    @NotNull String username,
                                    @NotNull String firstName,
                                    @NotNull String lastName) {
}
