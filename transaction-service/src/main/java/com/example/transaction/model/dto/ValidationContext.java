package com.example.transaction.model.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record ValidationContext(
    String initialWalletId,
    String targetWalletId,
    BigDecimal amount
) {

}
