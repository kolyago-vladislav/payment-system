package com.example.payment.provider.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "webhooks", schema = "payment_provider")
public class Webhook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "payload")
    @JdbcTypeCode(SqlTypes.JSON)
    private String payload;

    @Column(name = "received_at")
    private Instant receivedAt;

}