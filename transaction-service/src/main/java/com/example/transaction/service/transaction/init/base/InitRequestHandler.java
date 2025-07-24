package com.example.transaction.service.transaction.init.base;

import com.example.transaction.dto.InitRequest;
import com.example.transaction.dto.TransactionInitResponse;
import com.example.transaction.dto.TransactionType;

public interface InitRequestHandler {
    TransactionType getType();
    TransactionInitResponse handle(InitRequest request);
}
