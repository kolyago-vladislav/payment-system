package com.example.controller;

import reactor.core.publisher.Mono;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.example.individual.api.TransactionApi;
import com.example.individual.dto.ConfirmRequest;
import com.example.individual.dto.InitRequest;
import com.example.individual.dto.TransactionConfirmResponse;
import com.example.individual.dto.TransactionDto;
import com.example.individual.dto.TransactionInitResponse;
import com.example.individual.dto.TransactionPageDto;
import com.example.individual.dto.TransactionStatusDto;
import com.example.individual.dto.TransactionTypeDto;
import com.example.service.TransactionService;

@RestController
@RequiredArgsConstructor
public class TransactionController implements TransactionApi {

    private final TransactionService transactionService;

    @Override
    public Mono<ResponseEntity<TransactionConfirmResponse>> confirm(
        TransactionTypeDto type,
        Mono<ConfirmRequest> confirmRequest,
        ServerWebExchange exchange
    ) {
        return transactionService
            .confirm(type, confirmRequest)
            .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<TransactionPageDto>> findAll(
        List<String> userIds,
        List<String> walletIds,
        List<TransactionTypeDto> types,
        List<TransactionStatusDto> statuses,
        OffsetDateTime dateFrom,
        OffsetDateTime dateTo,
        Integer offset,
        Integer limit,
        ServerWebExchange exchange
    ) {
        return transactionService
            .findAll(userIds, walletIds, types, statuses, dateFrom, dateTo, offset, limit)
            .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<TransactionDto>> getStatus(
        String transactionId,
        ServerWebExchange exchange
    ) {
        return transactionService
            .findById(UUID.fromString(transactionId))
            .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<TransactionInitResponse>> init(
        TransactionTypeDto type,
        Mono<InitRequest> initRequest,
        ServerWebExchange exchange
    ) {
        return transactionService
            .init(type, initRequest)
            .map(ResponseEntity::ok);
    }
}
