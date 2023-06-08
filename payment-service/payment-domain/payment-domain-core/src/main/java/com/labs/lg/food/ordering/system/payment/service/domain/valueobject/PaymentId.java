package com.labs.lg.food.ordering.system.payment.service.domain.valueobject;

import com.labs.lg.pentagon.common.domain.valueobject.BaseId;
import java.util.UUID;

public class PaymentId extends BaseId<UUID> {

  public PaymentId(UUID value) {
    super(value);
  }
}
