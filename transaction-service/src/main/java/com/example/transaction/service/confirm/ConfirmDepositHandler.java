package com.example.transaction.service.confirm;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.example.transaction.dto.DepositConfirmRequest;
import com.example.transaction.dto.TransactionType;
import com.example.transaction.entity.Transaction;
import com.example.transaction.mapper.TransactionMapper;
import com.example.transaction.service.confirm.base.AbstractConfirmHandler;

@Component
@RequiredArgsConstructor
public class ConfirmDepositHandler extends AbstractConfirmHandler<DepositConfirmRequest> {

    private final TransactionMapper transactionMapper;

    @Override
    protected Transaction map(DepositConfirmRequest request) {
        return transactionMapper.to(request, getType());
    }

    @Override
    public TransactionType getType() {
        return TransactionType.DEPOSIT;
    }

    @Override
    public Class<DepositConfirmRequest> getRequestType() {
        return DepositConfirmRequest.class;
    }
}