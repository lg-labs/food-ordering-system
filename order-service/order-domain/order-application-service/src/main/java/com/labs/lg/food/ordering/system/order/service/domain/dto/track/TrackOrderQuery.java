package com.labs.lg.food.ordering.system.order.service.domain.dto.track;

import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
public record TrackOrderQuery(@NotNull UUID orderTrackingId) {
}
