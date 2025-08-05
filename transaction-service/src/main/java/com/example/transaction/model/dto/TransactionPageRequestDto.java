package com.example.transaction.model.dto;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.Builder;

import com.example.transaction.dto.TransactionStatusDto;
import com.example.transaction.dto.TransactionTypeDto;

@Builder
public record TransactionPageRequestDto(
    List<String> userIds,
    List<String> walletIds,
    List<TransactionTypeDto> types,
    List<TransactionStatusDto> statuses,
    OffsetDateTime dateFrom,
    OffsetDateTime dateTo,
    Integer offset,
    Integer limit
) {

}
