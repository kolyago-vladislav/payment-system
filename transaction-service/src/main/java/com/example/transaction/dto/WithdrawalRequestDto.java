package com.example.transaction.dto;

import java.time.Instant;
import java.util.UUID;

public record WithdrawalRequestDto(
    UUID transactionId,
    UUID userId,
    UUID walletId,
    double amount,
    String currency,
    String destination,
    Instant timestamp
) {}
