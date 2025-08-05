package com.example.transaction.model.dto;

import java.time.Instant;
import java.util.UUID;

import com.example.transaction.model.entity.type.ExternalServiceStatus;

public record WithdrawalCompletedDto(
    UUID transactionId,
    ExternalServiceStatus status,
    String failureReason,
    Instant timestamp
) {}