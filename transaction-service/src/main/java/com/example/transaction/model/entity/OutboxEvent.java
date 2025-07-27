package com.example.transaction.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.hibernate.annotations.ColumnTransformer;

import com.example.transaction.model.entity.type.TransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "outbox_events", schema = "transaction")
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @JsonProperty("created_at")
    @Column(name = "created_at")
    private Instant createdAt;

    @JsonProperty("transaction_id")
    @Column(name = "transaction_id")
    private UUID transactionId;

    @JsonProperty("trace_id")
    @Column(name = "trace_id")
    private String traceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @ColumnTransformer(write = "?::transaction.transaction_type")
    private TransactionType type;

    @ColumnTransformer(write = "?::jsonb")
    @Column(name = "payload", columnDefinition = "jsonb")
    private String payload;
}