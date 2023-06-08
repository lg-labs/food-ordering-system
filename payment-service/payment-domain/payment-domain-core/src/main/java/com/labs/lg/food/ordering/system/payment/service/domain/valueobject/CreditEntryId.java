package com.labs.lg.food.ordering.system.payment.service.domain.valueobject;

import com.labs.lg.pentagon.common.domain.valueobject.BaseId;
import java.util.UUID;

public class CreditEntryId extends BaseId<UUID> {

  public CreditEntryId(UUID value) {
    super(value);
  }
}
