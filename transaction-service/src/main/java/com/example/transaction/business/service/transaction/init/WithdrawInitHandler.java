package com.example.transaction.business.service.transaction.init;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.example.transaction.business.service.transaction.init.base.AbstractInitHandler;
import com.example.transaction.dto.TransactionInitResponse;
import com.example.transaction.dto.WithdrawalInitRequest;
import com.example.transaction.model.dto.ValidationContext;
import com.example.transaction.model.entity.type.TransactionType;

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
    protected ValidationContext getValidationContext(WithdrawalInitRequest dto) {
        return ValidationContext.builder()
            .sourceWalletId(dto.getWalletId())
            .amount(BigDecimal.valueOf(dto.getAmount()))
            .build();
    }

    @Override
    public Class<WithdrawalInitRequest> getRequestType() {
        return WithdrawalInitRequest.class;
    }
}