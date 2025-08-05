package com.example.transaction.business.service.transaction.init;

import java.math.BigDecimal;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.example.transaction.business.service.transaction.init.base.AbstractInitHandler;
import com.example.transaction.dto.DepositInitRequest;
import com.example.transaction.dto.TransactionInitResponse;
import com.example.transaction.model.dto.ValidationContext;
import com.example.transaction.model.entity.type.TransactionType;

@Component
@RequiredArgsConstructor
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
    protected ValidationContext getValidationContext(DepositInitRequest dto) {
        return ValidationContext.builder()
            .initialWalletId(dto.getWalletId())
            .amount(BigDecimal.valueOf(dto.getAmount()))
            .build();
    }

    @Override
    public Class<DepositInitRequest> getRequestType() {
        return DepositInitRequest.class;
    }
}