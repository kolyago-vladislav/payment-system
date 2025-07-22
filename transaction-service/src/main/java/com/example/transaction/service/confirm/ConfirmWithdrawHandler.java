package com.example.transaction.service.confirm;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.example.transaction.dto.TransactionType;
import com.example.transaction.dto.WithdrawalConfirmRequest;
import com.example.transaction.entity.Transaction;
import com.example.transaction.mapper.TransactionMapper;
import com.example.transaction.service.confirm.base.AbstractConfirmHandler;

@Component
@RequiredArgsConstructor
public class ConfirmWithdrawHandler extends AbstractConfirmHandler<WithdrawalConfirmRequest> {

    private final TransactionMapper transactionMapper;

    @Override
    protected Transaction map(WithdrawalConfirmRequest request) {
        return transactionMapper.to(request, getType());
    }

    @Override
    public TransactionType getType() {
        return TransactionType.WITHDRAWAL;
    }

    @Override
    public Class<WithdrawalConfirmRequest> getRequestType() {
        return WithdrawalConfirmRequest.class;
    }
}