package com.example.transaction.model.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record ValidationContext(
    String sourceWalletId,
    String targetWalletId,
    BigDecimal amount,
    String sourceCurrency,
    String targetCurrency
) {

}
