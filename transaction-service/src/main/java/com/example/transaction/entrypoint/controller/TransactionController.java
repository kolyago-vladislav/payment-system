package com.example.transaction.entrypoint.controller;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.transaction.api.TransactionApi;
import com.example.transaction.business.service.transaction.TransactionService;
import com.example.transaction.dto.ConfirmRequest;
import com.example.transaction.dto.InitRequest;
import com.example.transaction.dto.TransactionConfirmResponse;
import com.example.transaction.dto.TransactionDto;
import com.example.transaction.dto.TransactionInitResponse;
import com.example.transaction.dto.TransactionPageDto;
import com.example.transaction.dto.TransactionStatusDto;
import com.example.transaction.dto.TransactionTypeDto;
import com.example.transaction.model.dto.TransactionPageRequestDto;

@RestController
@RequiredArgsConstructor
public class TransactionController implements TransactionApi {

    private final TransactionService transactionService;

    @Override
    public ResponseEntity<TransactionInitResponse> init(
        TransactionTypeDto type,
        InitRequest initRequest
    ) {
        return ResponseEntity.ok(transactionService.init(type, initRequest));
    }

    @Override
    public ResponseEntity<TransactionConfirmResponse> confirm(
        TransactionTypeDto type,
        ConfirmRequest confirmRequest
    ) {
        return ResponseEntity.ok(transactionService.confirm(type, confirmRequest));
    }

    @Override
    public ResponseEntity<TransactionPageDto> findAll(
        List<String> userIds,
        List<String> walletIds,
        List<TransactionTypeDto> types,
        List<TransactionStatusDto> statuses,
        OffsetDateTime dateFrom,
        OffsetDateTime dateTo,
        Integer offset,
        Integer limit
    ) {
        var dto = TransactionPageRequestDto.builder()
            .dateFrom(dateFrom)
            .dateTo(dateTo)
            .statuses(statuses)
            .types(types)
            .userIds(userIds)
            .walletIds(walletIds)
            .limit(limit)
            .offset(offset)
            .build();

        return ResponseEntity.ok(transactionService.findAll(dto));
    }

    @Override
    public ResponseEntity<TransactionDto> getStatus(String transactionId) {
        return ResponseEntity.ok(transactionService.getStatus(transactionId));
    }
}
