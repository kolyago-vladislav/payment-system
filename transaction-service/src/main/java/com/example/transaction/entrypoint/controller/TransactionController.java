package com.example.transaction.entrypoint.controller;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.transaction.api.TransactionApi;
import com.example.transaction.dto.ConfirmRequest;
import com.example.transaction.dto.InitRequest;
import com.example.transaction.dto.TransactionConfirmResponse;
import com.example.transaction.dto.TransactionInitResponse;
import com.example.transaction.dto.TransactionStatusDto;
import com.example.transaction.dto.TransactionStatusResponse;
import com.example.transaction.business.service.transaction.TransactionService;
import com.example.transaction.dto.TransactionTypeDto;

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
    public ResponseEntity<List<TransactionStatusResponse>> findAll(
        String userUid,
        String walletUid,
        TransactionTypeDto type,
        TransactionStatusDto status,
        OffsetDateTime dateFrom,
        OffsetDateTime dateTo,
        Integer page,
        Integer size
    ) {
        return null;
    }

    @Override
    public ResponseEntity<TransactionStatusResponse> getStatus(String transactionId) {
        return null;
    }



}
