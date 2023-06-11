package com.labs.lg.food.ordering.system.saga;

import com.labs.lg.pentagon.common.domain.event.DomainEvent;

public interface SagaStep<T, S extends DomainEvent, U extends DomainEvent>{
  S process(T data);
  U rollback(T data);
}
