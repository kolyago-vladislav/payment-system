package com.example.transaction.service.transaction.confirm;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.example.transaction.dto.TransactionType;
import com.example.transaction.dto.TransferConfirmRequest;
import com.example.transaction.entity.Transaction;
import com.example.transaction.mapper.TransactionMapper;

@Component
@RequiredArgsConstructor
public class TransferConfirmHandler {

    private final TransactionMapper transactionMapper;

    protected Transaction map(TransferConfirmRequest request) {
        return transactionMapper.to(request, getType());
    }

    public TransactionType getType() {
        return TransactionType.TRANSFER;
    }

    protected void saveOutboxEvent(
        TransferConfirmRequest request,
        Transaction transaction
    ) {

    }

    public Class<TransferConfirmRequest> getRequestType() {
        return TransferConfirmRequest.class;
    }
}