package com.example.payment.provider.entrypoint.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.payment.provider.api.PaymentApi;
import com.example.payment.provider.business.service.TransactionService;
import com.example.payment.provider.dto.TransactionDto;
import com.example.payment.provider.dto.TransactionWriteDto;

@RestController
@RequiredArgsConstructor
public class TransactionController implements PaymentApi {

    private final TransactionService transactionService;

    @Override
    public ResponseEntity<TransactionDto> createTransaction(TransactionWriteDto transactionWriteDto) {
        return ResponseEntity.ok(transactionService.createTransaction(transactionWriteDto));
    }

    @Override
    public ResponseEntity<TransactionDto> findByTransactionId(Long id) {
        return ResponseEntity.ok(transactionService.findByTransactionId(id));
    }
}
