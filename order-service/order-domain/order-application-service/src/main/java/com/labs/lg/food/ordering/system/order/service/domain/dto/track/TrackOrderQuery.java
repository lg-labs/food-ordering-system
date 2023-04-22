package com.labs.lg.food.ordering.system.order.service.domain.dto.track;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public record TrackOrderQuery(@NotNull UUID orderTrackingId) {
}
