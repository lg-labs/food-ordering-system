package com.labs.lg.food.ordering.system.outbox;

public interface OutboxScheduler {
    void processOutboxMessage();
}
