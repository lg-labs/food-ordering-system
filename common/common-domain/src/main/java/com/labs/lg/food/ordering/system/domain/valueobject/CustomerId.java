package com.labs.lg.food.ordering.system.domain.valueobject;

import com.labs.lg.pentagon.common.domain.valueobject.BaseId;

import java.util.UUID;

public class CustomerId extends BaseId<UUID> {
    public CustomerId(UUID value) {
        super(value);
    }
}
