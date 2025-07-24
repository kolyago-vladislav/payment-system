package com.example.transaction.service.outbox.event.base;

import com.example.transaction.entity.OutboxEvent;
import com.example.transaction.entity.type.TransactionType;

public interface OutboxEventHandler<T> {

    TransactionType getOutboxEventType();

    void handle(OutboxEvent outboxEvent);

    Class<T> getType();
}
