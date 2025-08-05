package com.example.transaction.business.service.transaction.init.base;

import com.example.transaction.dto.InitRequest;
import com.example.transaction.dto.TransactionInitResponse;
import com.example.transaction.model.entity.type.TransactionType;

public interface InitRequestHandler {
    TransactionType getType();
    TransactionInitResponse handle(InitRequest request);
}
