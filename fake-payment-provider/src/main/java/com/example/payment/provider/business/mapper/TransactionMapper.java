package com.example.payment.provider.business.mapper;

import lombok.Setter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.payment.provider.business.repository.MerchantRepository;
import com.example.payment.provider.core.exception.PaymentProviderException;
import com.example.payment.provider.core.util.DateTimeUtil;
import com.example.payment.provider.core.util.SecurityUtils;
import com.example.payment.provider.dto.TransactionDto;
import com.example.payment.provider.dto.TransactionWriteDto;
import com.example.payment.provider.model.entity.Merchant;
import com.example.payment.provider.model.entity.Transaction;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
@Setter(onMethod_ = {@Autowired})
public abstract class TransactionMapper {

    protected MerchantRepository merchantRepository;
    protected DateTimeUtil dateTimeUtil;

    @Mapping(target = "createdAt", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "updatedAt", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "merchant", expression = "java(getMerchant())")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "retryCount", constant = "1")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "method", source = "method")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "externalId", constant = "EXTERNAL_ID_1")
    public abstract Transaction to(TransactionWriteDto dto);

    public Merchant getMerchant() {
        return merchantRepository.findByName(SecurityUtils.getMerchantName())
            .orElseThrow(() -> new PaymentProviderException("Merchant not found: " + SecurityUtils.getMerchantName()));
    }

    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", expression = "java(dateTimeUtil.to(transaction.getCreatedAt()))")
    @Mapping(target = "updatedAt", expression = "java(dateTimeUtil.to(transaction.getUpdatedAt()))")
    @Mapping(target = "merchantId", source = "merchant.id")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "method", source = "method")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "externalId", source = "externalId")
    public abstract TransactionDto from(Transaction transaction);
}
