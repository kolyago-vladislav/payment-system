package com.example.transaction.service.outbox;

import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.example.transaction.entity.type.TransactionType;
import com.example.transaction.service.outbox.event.base.OutboxEventHandler;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final Map<TransactionType, OutboxEventHandler<?>> eventHandlers;

    public void process(com.example.transaction.entity.OutboxEvent outboxEvent) {
        eventHandlers.get(outboxEvent.getType()).handle(outboxEvent);
    }
}
