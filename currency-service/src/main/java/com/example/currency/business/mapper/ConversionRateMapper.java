package com.example.currency.business.mapper;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.Setter;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.currency.core.util.DateTimeUtil;
import com.example.currency.dto.RateDto;
import com.example.currency.model.entity.ConversionRate;

@Mapper(componentModel = "spring")
@Setter(onMethod_ = {@Autowired})
public abstract class ConversionRateMapper {

    protected DateTimeUtil dateTimeUtil;

    @Mapping(target = "sourceCode", source = "sourceCode")
    @Mapping(target = "targetCode", source = "targetCode")
    @Mapping(target = "rate", source = "rate")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "rateBeginTime", source = "rateBeginTime")
    @Mapping(target = "providerId", source = "providerId")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "createdAt", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "updatedAt", expression = "java(dateTimeUtil.now())")
    public abstract ConversionRate toEntity(
        String sourceCode,
        String targetCode,
        BigDecimal rate,
        Integer amount,
        Instant rateBeginTime,
        String providerId
    );

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "source", source = "sourceCode")
    @Mapping(target = "target", source = "targetCode")
    @Mapping(target = "rate", source = "rate")
    @Mapping(target = "providerCode", source = "providerId")
    public abstract RateDto from(ConversionRate conversionRate);
}