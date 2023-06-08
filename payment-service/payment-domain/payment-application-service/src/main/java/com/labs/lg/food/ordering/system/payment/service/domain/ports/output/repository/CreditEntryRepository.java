package com.labs.lg.food.ordering.system.payment.service.domain.ports.output.repository;

import com.labs.lg.food.ordering.system.domain.valueobject.CustomerId;
import com.labs.lg.food.ordering.system.payment.service.domain.entity.CreditEntry;

import java.util.Optional;

public interface CreditEntryRepository {
  CreditEntry save(CreditEntry creditEntry);
  Optional<CreditEntry> findByCustomerId(CustomerId customerId);
}
