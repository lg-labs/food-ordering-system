package com.labs.lg.food.ordering.system.order.service.domain;

import com.labs.lg.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.labs.lg.pentagon.common.domain.event.publisher.DomainEventPublisher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationEventDomainPublisher implements
    ApplicationEventPublisherAware, DomainEventPublisher<OrderCreatedEvent> {

  private ApplicationEventPublisher applicationEventPublisher;
  @Override
  public void setApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Override
  public void publish(OrderCreatedEvent domainEvent) {
    applicationEventPublisher
        .publishEvent(domainEvent);
    log.info("OrderCreatedEvent is published for order id: {}", domainEvent.getOrder().getId().getValue());
  }
}
