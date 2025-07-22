package com.example.transaction.service.confirm.base;

import com.example.transaction.dto.ConfirmRequest;
import com.example.transaction.dto.TransactionConfirmResponse;
import com.example.transaction.dto.TransactionType;

public interface ConfirmRequestHandler {
    TransactionType getType();
    TransactionConfirmResponse handle(ConfirmRequest request);
}
