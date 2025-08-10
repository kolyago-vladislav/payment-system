package com.example.transaction.business.mapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

import lombok.Setter;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.transaction.core.util.DateTimeUtil;
import com.example.transaction.dto.DepositConfirmRequest;
import com.example.transaction.dto.TransactionConfirmResponse;
import com.example.transaction.dto.TransactionDto;
import com.example.transaction.dto.TransferConfirmRequest;
import com.example.transaction.dto.WithdrawalConfirmRequest;
import com.example.transaction.model.entity.Transaction;
import com.example.transaction.model.entity.type.TransactionType;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
@Setter(onMethod_ = {@Autowired})
public abstract class TransactionMapper {

    protected DateTimeUtil dateTimeUtil;

    @Mapping(target = "transactionId", source = "id")
    @Mapping(target = "status", source = "status")
    public abstract TransactionConfirmResponse from(Transaction transaction);

    @Mapping(target = "createdAt", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "updatedAt", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "walletId", source = "request.walletId")
    @Mapping(target = "amount", source = "request.amount")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "fee", source = "request.fee")
    @Mapping(target = "userId", source = "request.userId")
    @Mapping(target = "paymentMethodId", source = "request.paymentMethodId")
    public abstract Transaction to(
        DepositConfirmRequest request,
        TransactionType type
    );

    @Mapping(target = "createdAt", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "updatedAt", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "walletId", source = "fromWalletId")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "targetAmount", source = ".", qualifiedByName = "toTargetAmount")
    @Mapping(target = "type", expression = "java(type)")
    @Mapping(target = "status", constant = "COMPLETED")
    @Mapping(target = "fee", source = "fee")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "targetWalletId", source = "toWalletId")
    public abstract Transaction to(
        TransferConfirmRequest request,
        @Context
        TransactionType type
    );

    @Named("toTargetAmount")
    protected BigDecimal toTargetAmount(
        TransferConfirmRequest request
    ) {
        return BigDecimal.valueOf(request.getAmount()).multiply(BigDecimal.valueOf(request.getConversionRate()));
    }

    @Mapping(target = "createdAt", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "updatedAt", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "walletId", source = "request.walletId")
    @Mapping(target = "amount", source = "request.amount")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "fee", source = "request.fee")
    @Mapping(target = "userId", source = "request.userId")
    @Mapping(target = "paymentMethodId", source = "request.paymentMethodId")
    public abstract Transaction to(
        WithdrawalConfirmRequest request,
        TransactionType type
    );

    @Mapping(target = "transactionId", source = "id")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "currency", source = "wallet.walletType.currencyCode")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "toOffsetDateTime")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "toOffsetDateTime" )
    public abstract TransactionDto toTransactionDto(Transaction transaction);

    public abstract List<TransactionDto> from(List<Transaction> transactions);

    @Named("toOffsetDateTime")
    protected OffsetDateTime toOffsetDateTime(Instant instant) {
        return dateTimeUtil.to(instant);
    }
}
