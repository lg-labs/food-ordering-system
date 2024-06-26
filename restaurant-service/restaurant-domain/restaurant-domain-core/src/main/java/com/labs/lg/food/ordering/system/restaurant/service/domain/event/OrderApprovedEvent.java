package com.labs.lg.food.ordering.system.restaurant.service.domain.event;

import com.labs.lg.food.ordering.system.restaurant.service.domain.valueobject.RestaurantId;
import com.labs.lg.food.ordering.system.restaurant.service.domain.entity.OrderApproval;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderApprovedEvent extends OrderApprovalEvent {
    public OrderApprovedEvent(OrderApproval orderApproval,
                              RestaurantId restaurantId,
                              List<String> failureMessages,
                              ZonedDateTime createdAt) {
        super(orderApproval, restaurantId, failureMessages, createdAt);
    }
}
