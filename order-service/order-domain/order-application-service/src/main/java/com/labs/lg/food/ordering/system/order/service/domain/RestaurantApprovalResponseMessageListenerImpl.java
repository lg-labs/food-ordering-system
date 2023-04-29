package com.labs.lg.food.ordering.system.order.service.domain;

import com.labs.lg.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.labs.lg.food.ordering.system.order.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
public class RestaurantApprovalResponseMessageListenerImpl implements RestaurantApprovalResponseMessageListener {
  @Override
  public void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse) {
    // implementing
  }

  @Override
  public void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse) {
    // implementing
  }
}
