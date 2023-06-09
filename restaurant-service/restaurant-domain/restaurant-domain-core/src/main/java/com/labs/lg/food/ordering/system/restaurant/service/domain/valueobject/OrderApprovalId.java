package com.labs.lg.food.ordering.system.restaurant.service.domain.valueobject;

import com.labs.lg.pentagon.common.domain.valueobject.BaseId;

import java.util.UUID;

public class OrderApprovalId extends BaseId<UUID> {
  public OrderApprovalId(UUID value) {
    super(value);
  }
}
