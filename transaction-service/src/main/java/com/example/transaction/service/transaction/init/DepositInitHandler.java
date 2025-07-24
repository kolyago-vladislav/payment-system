package com.example.transaction.service.transaction.init;

import org.springframework.stereotype.Component;

import com.example.transaction.dto.DepositInitRequest;
import com.example.transaction.dto.TransactionInitResponse;
import com.example.transaction.dto.TransactionType;
import com.example.transaction.service.transaction.init.base.AbstractInitHandler;

@Component
public class DepositInitHandler extends AbstractInitHandler<DepositInitRequest> {

    @Override
    public TransactionType getType() {
        return TransactionType.DEPOSIT;
    }

    @Override
    public TransactionInitResponse doAction(DepositInitRequest request) {
        return new TransactionInitResponse().fee(10.0);
    }

    @Override
    public Class<DepositInitRequest> getRequestType() {
        return DepositInitRequest.class;
    }
}