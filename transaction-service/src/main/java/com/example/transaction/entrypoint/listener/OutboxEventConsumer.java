package com.example.transaction.entrypoint.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.transaction.business.service.outbox.OutboxService;
import com.example.transaction.core.util.JsonWrapper;
import com.example.transaction.core.util.TracingUtil;
import com.example.transaction.model.entity.OutboxEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxEventConsumer {

    private final OutboxService outboxService;
    private final JsonWrapper jsonWrapper;

    @KafkaListener(
        topics = "postgres.transaction.outbox_events",
        groupId = "transaction-service",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void processMessage(byte[] event) {
        var outboxEvent = jsonWrapper.read(event, OutboxEvent.class);
        TracingUtil.withTraceContext(outboxEvent.getTraceId(), () -> {
            log.debug("OutboxEventConsumer.processMessage: {}", outboxEvent.getTransactionId());
            outboxService.process(outboxEvent);
        });
    }
}
