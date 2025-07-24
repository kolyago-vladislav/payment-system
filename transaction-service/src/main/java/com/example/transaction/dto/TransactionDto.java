package com.example.transaction.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.transaction.entity.type.TransactionStatus;
import com.example.transaction.entity.type.TransactionType;
import com.example.transaction.util.BigDecimalFromObjectDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public record TransactionDto (
    UUID id,
    @JsonProperty("user_id")
    UUID userId,
    @JsonProperty("wallet_id")
    UUID walletId,
    @JsonDeserialize(using = BigDecimalFromObjectDeserializer.class)
    BigDecimal amount,
    TransactionType type,
    TransactionStatus status,
    String comment,
    @JsonDeserialize(using = BigDecimalFromObjectDeserializer.class)
    BigDecimal fee,
    @JsonProperty("target_wallet_id")
    UUID targetWalletId,
    @JsonProperty("payment_method_id")
    Long paymentMethodId,
    String failureReason
) {

}
