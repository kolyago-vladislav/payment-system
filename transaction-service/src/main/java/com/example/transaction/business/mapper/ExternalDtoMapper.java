package com.example.transaction.business.mapper;

import lombok.Setter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.transaction.core.util.DateTimeUtil;
import com.example.transaction.model.dto.DepositRequestDto;
import com.example.transaction.model.dto.WithdrawalRequestDto;
import com.example.transaction.model.entity.Transaction;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
@Setter(onMethod_ = {@Autowired})
public abstract class ExternalDtoMapper {
    protected DateTimeUtil dateTimeUtil;

    @Mapping(target = "transactionId", source = "id")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "walletId", source = "wallet.id")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "currency", source = "wallet.walletType.currencyCode")
    @Mapping(target = "timestamp", expression = "java(dateTimeUtil.now())")
    public abstract DepositRequestDto toDepositRequestDto(Transaction transaction);

    @Mapping(target = "transactionId", source = "id")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "walletId", source = "wallet.id")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "currency", source = "wallet.walletType.currencyCode")
    @Mapping(target = "timestamp", expression = "java(dateTimeUtil.now())")
    public abstract WithdrawalRequestDto toWithdrawalRequestDto(Transaction transaction);
}
