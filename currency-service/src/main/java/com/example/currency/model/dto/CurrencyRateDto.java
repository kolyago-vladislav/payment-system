package com.example.currency.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record CurrencyRateDto(
    Integer amount,
    String base,
    LocalDate date,
    Map<String, BigDecimal> rates
) {

}
