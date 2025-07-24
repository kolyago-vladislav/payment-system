package com.example.transaction.service.transaction.init;

import org.springframework.stereotype.Component;

import com.example.transaction.dto.TransactionInitResponse;
import com.example.transaction.dto.TransactionType;
import com.example.transaction.dto.TransferInitRequest;
import com.example.transaction.service.transaction.init.base.AbstractInitHandler;

@Component
public class TransferInitHandler extends AbstractInitHandler<TransferInitRequest> {

    @Override
    public TransactionType getType() {
        return TransactionType.TRANSFER;
    }

    @Override
    public TransactionInitResponse doAction(TransferInitRequest request) {
        return new TransactionInitResponse().fee(20.0);
    }

    @Override
    public Class<TransferInitRequest> getRequestType() {
        return TransferInitRequest.class;
    }
}