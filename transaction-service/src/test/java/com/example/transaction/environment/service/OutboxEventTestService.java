package com.example.transaction.environment.service;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.example.transaction.business.repository.OutboxRepository;
import com.example.transaction.model.entity.OutboxEvent;

@Service
@RequiredArgsConstructor
public class OutboxEventTestService {

    private final OutboxRepository outboxRepository;

    public OutboxEvent getOutboxEventByTransactionId(String transactionId) {
        return outboxRepository.findByTransactionId(UUID.fromString(transactionId)).get();
    }
}
