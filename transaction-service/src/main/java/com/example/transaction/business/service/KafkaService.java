package com.example.transaction.business.service;

import lombok.RequiredArgsConstructor;

import org.slf4j.MDC;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static com.example.transaction.core.util.TransactionCons.TRACE_ID_KEY;

@Service
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, String key, Object payload) {
        var message = MessageBuilder
            .withPayload(payload)
            .setHeader(KafkaHeaders.TOPIC, topic)
            .setHeader(KafkaHeaders.KEY, key)
            .setHeader(TRACE_ID_KEY, MDC.get(TRACE_ID_KEY))
            .build();

        kafkaTemplate.send(message).join();
    }
}
