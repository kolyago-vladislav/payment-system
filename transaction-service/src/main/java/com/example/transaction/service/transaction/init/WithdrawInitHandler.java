package com.example.transaction.service.transaction.init;

import org.springframework.stereotype.Component;

import com.example.transaction.dto.TransactionInitResponse;
import com.example.transaction.dto.WithdrawalInitRequest;
import com.example.transaction.entity.type.TransactionType;
import com.example.transaction.service.transaction.init.base.AbstractInitHandler;

@Component
public class WithdrawInitHandler extends AbstractInitHandler<WithdrawalInitRequest> {

    @Override
    public TransactionType getType() {
        return TransactionType.WITHDRAWAL;
    }

    @Override
    public TransactionInitResponse doAction(WithdrawalInitRequest request) {
        return new TransactionInitResponse().fee(30.0);
    }

    @Override
    public Class<WithdrawalInitRequest> getRequestType() {
        return WithdrawalInitRequest.class;
    }
}