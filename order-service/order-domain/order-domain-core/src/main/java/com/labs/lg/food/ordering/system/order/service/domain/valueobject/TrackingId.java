package com.labs.lg.food.ordering.system.order.service.domain.valueobject;


import com.labs.lg.pentagon.common.domain.valueobject.BaseId;

import java.util.UUID;

public class TrackingId extends BaseId<UUID> {
    public TrackingId(UUID value) {
        super(value);
    }
}
