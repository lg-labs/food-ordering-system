package com.labs.lg.food.ordering.system.order.service.domain.dto.create;

import com.labs.lg.food.ordering.system.domain.valueobject.OrderStatus;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
public record CreateOrderResponse(@NotNull UUID orderTrackingId,
                                  @NotNull OrderStatus orderStatus,
                                  @NotNull String message) {

}
