package com.example.transaction.business.service.outbox;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.example.transaction.model.entity.OutboxEvent;
import com.example.transaction.model.entity.type.TransactionType;
import com.example.transaction.business.service.outbox.event.base.OutboxEventHandler;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {

    private final Map<TransactionType, OutboxEventHandler<?>> eventHandlers;

    public void process(OutboxEvent outboxEvent) {
        log.debug("Processing outbox event: {}", outboxEvent);
        eventHandlers.get(outboxEvent.getType()).handle(outboxEvent);
    }
}
