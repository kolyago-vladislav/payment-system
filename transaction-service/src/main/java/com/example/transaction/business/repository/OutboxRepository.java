package com.example.transaction.business.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transaction.model.entity.OutboxEvent;

public interface OutboxRepository extends JpaRepository<OutboxEvent, UUID> {
    Optional<OutboxEvent> findByTransactionId(UUID transactionId);
}
