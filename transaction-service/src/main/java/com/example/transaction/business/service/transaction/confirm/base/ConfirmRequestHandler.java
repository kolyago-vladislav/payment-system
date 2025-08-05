package com.example.transaction.business.service.transaction.confirm.base;

import com.example.transaction.dto.ConfirmRequest;
import com.example.transaction.dto.TransactionConfirmResponse;
import com.example.transaction.model.entity.type.TransactionType;

public interface ConfirmRequestHandler {
    TransactionType getType();
    TransactionConfirmResponse handle(ConfirmRequest request);
}
