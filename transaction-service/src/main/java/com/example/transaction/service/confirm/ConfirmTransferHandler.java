package com.example.transaction.service.confirm;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.example.transaction.dto.TransactionType;
import com.example.transaction.dto.TransferConfirmRequest;
import com.example.transaction.entity.Transaction;
import com.example.transaction.mapper.TransactionMapper;
import com.example.transaction.service.confirm.base.AbstractConfirmHandler;

@Component
@RequiredArgsConstructor
public class ConfirmTransferHandler extends AbstractConfirmHandler<TransferConfirmRequest> {

    private final TransactionMapper transactionMapper;

    @Override
    protected Transaction map(TransferConfirmRequest request) {
        return transactionMapper.to(request, getType());
    }

    @Override
    public TransactionType getType() {
        return TransactionType.TRANSFER;
    }

    @Override
    public Class<TransferConfirmRequest> getRequestType() {
        return TransferConfirmRequest.class;
    }
}