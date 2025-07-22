package com.example.transaction.mapper;

import java.util.UUID;

import lombok.Setter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.transaction.dto.DepositConfirmRequest;
import com.example.transaction.dto.TransactionConfirmResponse;
import com.example.transaction.dto.TransactionType;
import com.example.transaction.dto.TransferConfirmRequest;
import com.example.transaction.dto.WithdrawalConfirmRequest;
import com.example.transaction.entity.Transaction;
import com.example.transaction.entity.Wallet;
import com.example.transaction.exception.TransactionServiceException;
import com.example.transaction.repository.WalletRepository;
import com.example.transaction.util.DateTimeUtil;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
@Setter(onMethod_ = {@Autowired})
public abstract class TransactionMapper {

    protected DateTimeUtil dateTimeUtil;
    private WalletRepository walletRepository;

    @Mapping(target = "transactionId", source = "id")
    @Mapping(target = "status", source = "status")
    public abstract TransactionConfirmResponse from(Transaction transaction);

    @Mapping(target = "created", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "updated", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "wallet", source = "request.walletId", qualifiedByName = "toWallet")
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

    @Named("toWallet")
    protected Wallet toWallet(String walletId) {
        return walletRepository.findById(UUID.fromString(walletId))
            .orElseThrow(() -> new TransactionServiceException("Cannot find wallet with id: %s", walletId));
    }

    @Mapping(target = "created", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "updated", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "wallet", source = "request.fromWalletId", qualifiedByName = "toWallet")
    @Mapping(target = "amount", source = "request.amount")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "fee", source = "request.fee")
    @Mapping(target = "userId", source = "request.userId")
    @Mapping(target = "targetWallet", source = "request.toWalletId", qualifiedByName = "toWallet")
    public abstract Transaction to(
        TransferConfirmRequest request,
        TransactionType type
    );

    @Mapping(target = "created", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "updated", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "wallet", source = "request.walletId", qualifiedByName = "toWallet")
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
}
