package com.labs.lg.food.ordering.system.domain.valueobject;

import com.labs.lg.pentagon.common.domain.valueobject.BaseId;

import java.util.UUID;

public class RestaurantId extends BaseId<UUID> {

    public RestaurantId(UUID value) {
        super(value);
    }
}
