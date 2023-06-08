package com.labs.lg.food.ordering.system.payment.service.domain.ports.output.message.publisher;

import com.labs.lg.food.ordering.system.payment.service.domain.event.PaymentFailedEvent;
import com.labs.lg.pentagon.common.domain.event.publisher.DomainEventPublisher;

public interface PaymentFailedMessagePublisher
  extends DomainEventPublisher<PaymentFailedEvent> {}
