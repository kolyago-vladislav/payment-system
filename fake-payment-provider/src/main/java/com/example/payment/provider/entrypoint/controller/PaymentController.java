package com.example.payment.provider.entrypoint.controller;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.payment.api.PaymentApi;
import com.example.payment.dto.Transaction;
import com.example.payment.dto.TransactionRequest;

@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentApi {

    @Override
    public ResponseEntity<Transaction> createTransaction(TransactionRequest transactionRequest) {
        return null;
    }

    @Override
    public ResponseEntity<Transaction> findByTransactionId(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<List<Transaction>> findTransactions(
        OffsetDateTime startDate,
        OffsetDateTime endDate
    ) {
        return null;
    }
}
