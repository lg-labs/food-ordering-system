package com.labs.lg.food.ordering.system.order.service.domain.valueobject;

import com.labs.lg.pentagon.common.domain.valueobject.BaseId;

public class OrderItemId extends BaseId<Long> {
    public OrderItemId(Long value) {
        super(value);
    }
}
