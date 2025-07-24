package com.example.transaction.entrypoint.listener;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.transaction.model.entity.OutboxEvent;
import com.example.transaction.business.service.outbox.OutboxService;
import com.example.transaction.core.util.JsonWrapper;

@Service
@RequiredArgsConstructor
public class OutboxEventConsumer {

    private final OutboxService outboxService;
    private final JsonWrapper jsonWrapper;

    @KafkaListener(topics = "postgres.transaction.outbox_events", groupId = "transaction-service", containerFactory = "kafkaListenerContainerFactory")
    public void processMessage(byte[] event) {
        var outboxEvent = jsonWrapper.read(event, OutboxEvent.class);

        outboxService.process(outboxEvent);
    }
}
