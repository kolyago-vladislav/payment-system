package com.example.currency.business.mapper;

import java.util.List;

import lombok.Setter;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.currency.dto.RateProviderDto;
import com.example.currency.model.entity.RateProvider;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
@Setter(onMethod_ = {@Autowired})
public abstract class RateProviderMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "code", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "priority", source = "priority")
    @Mapping(target = "active", source = "active")
    public abstract RateProviderDto from(RateProvider rateProvider);

    public abstract List<RateProviderDto> from(List<RateProvider> currencies);

}
