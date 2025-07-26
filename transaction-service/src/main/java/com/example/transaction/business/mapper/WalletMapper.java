package com.example.transaction.business.mapper;

import java.util.List;
import java.util.UUID;

import lombok.Setter;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.transaction.business.repository.WalletTypeRepository;
import com.example.transaction.core.exception.TransactionServiceException;
import com.example.transaction.core.util.DateTimeUtil;
import com.example.transaction.dto.WalletDto;
import com.example.transaction.dto.WalletWriteDto;
import com.example.transaction.model.entity.Wallet;
import com.example.transaction.model.entity.WalletType;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
@Setter(onMethod_ = {@Autowired})
public abstract class WalletMapper {

    protected DateTimeUtil dateTimeUtil;
    private WalletTypeRepository walletTypeRepository;

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "createdAt", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "updatedAt", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "walletType", source = "walletTypeId", qualifiedByName = "toWalletType" )
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "balance", constant = "0")
    public abstract Wallet to(WalletWriteDto walletWriteDto);

    @Named("toWalletType")
    public WalletType findById(UUID walletTypeId) {
        return walletTypeRepository.findById(walletTypeId)
            .orElseThrow(() -> new TransactionServiceException("Cannot find wallet type with id: %s", walletTypeId.toString()));
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "walletType", source = "walletType.name")
    @Mapping(target = "currency", source = "walletType.currencyCode")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "balance", source = "balance")
    public abstract WalletDto from(Wallet wallet);

    public abstract List<WalletDto> from(List<Wallet> wallets);
}
