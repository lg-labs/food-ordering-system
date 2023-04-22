package com.labs.lg.food.ordering.system.domain.event.publisher;

import com.labs.lg.food.ordering.system.domain.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {
  void publish(T domainEvent);
}
