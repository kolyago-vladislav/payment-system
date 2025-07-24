package com.example.transaction.model.dto;

import java.time.Instant;
import java.util.UUID;

import com.example.transaction.model.entity.type.ExternalServiceStatus;

public record DepositCompletedDto(
    UUID transactionId,
    ExternalServiceStatus status,
    double amount,
    Instant timestamp
) {

}
