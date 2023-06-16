package com.labs.lg.food.ordering.system.restaurant.service.domain.mapper;

import com.labs.lg.food.ordering.system.domain.valueobject.OrderId;
import com.labs.lg.food.ordering.system.domain.valueobject.OrderStatus;
import com.labs.lg.food.ordering.system.domain.valueobject.RestaurantId;
import com.labs.lg.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.labs.lg.food.ordering.system.restaurant.service.domain.entity.OrderDetail;
import com.labs.lg.food.ordering.system.restaurant.service.domain.entity.Product;
import com.labs.lg.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import com.labs.lg.pentagon.common.domain.valueobject.Money;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * <strong> This class will be used as a factory:  </strong> <br>
 *
 * That will create the Domain Objects from the Input Objects. <br>
 * Also, will create the Output Objects from Domain Objects
 */
@Component
public class RestaurantDataMapper {
  public Restaurant restaurantApprovalRequestToRestaurant(RestaurantApprovalRequest restaurantApprovalRequest) {
    return Restaurant.builder()
        .restaurantId(new RestaurantId(UUID.fromString(restaurantApprovalRequest.restaurantId())))
        .orderDetail(OrderDetail.builder()
            .orderId(new OrderId(UUID.fromString(restaurantApprovalRequest.orderId())))
            .products(restaurantApprovalRequest.products().stream().map(
                    product -> Product.builder()
                        .productId(product.getId())
                        .quantity(product.getQuantity())
                        .build())
                .toList())
            .totalAmount(new Money(restaurantApprovalRequest.price()))
            .orderStatus(OrderStatus.valueOf(restaurantApprovalRequest.restaurantOrderStatus().name()))
            .build())
        .build();
  }
}
