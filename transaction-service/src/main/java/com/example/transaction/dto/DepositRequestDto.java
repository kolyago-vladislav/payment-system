package com.example.transaction.dto;

import java.time.Instant;
import java.util.UUID;

public record DepositRequestDto(
    UUID transactionId,
    UUID userId,
    UUID walletId,
    double amount,
    String currency,
    Instant timestamp
) {}
