package com.labs.lg.food.ordering.system.order.service.domain.dto.create;

import lombok.Builder;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.UUID;


@Builder
public record OrderAddress(@NotNull UUID id,
                           @NotNull @Max(value = 50) String street,
                           @NotNull @Max(value = 10) String postalCode,
                           @NotNull @Max(value = 50) String city) {

}
