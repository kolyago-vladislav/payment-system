package com.example.transaction.business.service.transaction.init;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.example.transaction.business.service.transaction.init.base.AbstractInitHandler;
import com.example.transaction.dto.TransactionInitResponse;
import com.example.transaction.dto.TransferInitRequest;
import com.example.transaction.model.dto.ValidationContext;
import com.example.transaction.model.entity.type.TransactionType;

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
    protected ValidationContext getValidationContext(TransferInitRequest dto) {
        return ValidationContext.builder()
            .initialWalletId(dto.getFromWalletId())
            .targetWalletId(dto.getToWalletId())
            .amount(BigDecimal.valueOf(dto.getAmount()))
            .build();
    }

    @Override
    public Class<TransferInitRequest> getRequestType() {
        return TransferInitRequest.class;
    }
}