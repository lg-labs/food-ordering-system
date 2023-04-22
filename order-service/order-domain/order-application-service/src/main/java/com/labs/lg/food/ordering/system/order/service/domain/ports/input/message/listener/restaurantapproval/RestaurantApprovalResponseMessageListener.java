package com.labs.lg.food.ordering.system.order.service.domain.ports.input.message.listener.restaurantapproval;

import com.labs.lg.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.labs.lg.food.ordering.system.order.service.domain.entity.Order;

public interface RestaurantApprovalResponseMessageListener {

  void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse);
  void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse);
}
