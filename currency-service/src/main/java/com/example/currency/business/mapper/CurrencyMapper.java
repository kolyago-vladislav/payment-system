package com.example.currency.business.mapper;

import java.util.List;

import lombok.Setter;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.currency.dto.CurrencyDto;
import com.example.currency.model.entity.Currency;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
@Setter(onMethod_ = {@Autowired})
public abstract class CurrencyMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "code", source = "code")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "isoCode", source = "isoCode")
    @Mapping(target = "symbol", source = "symbol")
    public abstract CurrencyDto from(Currency currency);

    public abstract List<CurrencyDto> from(List<Currency> currencies);

}
