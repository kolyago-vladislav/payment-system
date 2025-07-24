package com.example.transaction.model.dto;

import java.time.Instant;
import java.util.UUID;

public record DepositCompleted(
    UUID transactionId,
    String status,

    double amount,
    Instant timestamp
) {

}
