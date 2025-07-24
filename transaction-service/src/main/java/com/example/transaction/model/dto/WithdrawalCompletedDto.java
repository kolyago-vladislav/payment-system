package com.example.transaction.model.dto;

import java.time.Instant;
import java.util.UUID;

public record WithdrawalCompletedDto(
    UUID transactionId,
    String status,
    String failureReason,
    Instant timestamp
) {}