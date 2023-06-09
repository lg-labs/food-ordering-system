package com.labs.lg.food.ordering.system.restaurant.service.domain.dto;

import com.labs.lg.food.ordering.system.domain.valueobject.RestaurantOrderStatus;
import com.labs.lg.food.ordering.system.restaurant.service.domain.entity.Product;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Builder
public record RestaurantApprovalRequest(String id, String sagaId, String restaurantId, String orderId,
                                        RestaurantOrderStatus restaurantOrderStatus,
                                        List<Product>  products,
                                        BigDecimal price,
                                        Instant createdAt) {
}
