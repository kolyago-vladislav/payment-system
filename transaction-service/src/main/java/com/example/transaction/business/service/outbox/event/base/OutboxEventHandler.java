package com.example.transaction.business.service.outbox.event.base;

import com.example.transaction.model.entity.OutboxEvent;
import com.example.transaction.model.entity.type.TransactionType;

public interface OutboxEventHandler<T> {

    TransactionType getOutboxEventType();

    void handle(OutboxEvent outboxEvent);

    Class<T> getType();
}
