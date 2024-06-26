package com.labs.lg.food.ordering.system.order.service.domain.dto.message;

import com.labs.lg.food.ordering.system.order.service.domain.valueobject.OrderApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

/**
 * At communicate between services, it will use a field called {@link #sagaId}
 * E.g: The services are: OrderService & PaymentService, are different bounded contexts.
 * <p>
 */
@Getter
@Builder
@AllArgsConstructor
public class RestaurantApprovalResponse {
    private String id;
    private String sagaId;
    private String orderId;
    private String restaurantId;
    private Instant createdAt;
    private OrderApprovalStatus orderApprovalStatus;
    private List<String> failureMessages;


}
