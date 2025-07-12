package com.example.mapper;

import lombok.Setter;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.entity.Individual;
import com.example.person.dto.IndividualDto;
import com.example.person.dto.IndividualWriteDto;
import com.example.util.DateTimeUtil;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
    componentModel = SPRING,
    injectionStrategy = CONSTRUCTOR,
    uses = {
        UserMapper.class,
        AddressMapper.class,
    }
)
@Setter(onMethod_ = @Autowired)
public abstract class IndividualMapper {

    protected DateTimeUtil dateTimeUtil;

    @Mapping(target = "active", constant = "true")
    @Mapping(target = "created", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "updated", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "user", source = ".", qualifiedByName = "toUser")
    public abstract Individual to(IndividualWriteDto dto);

    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "address", source = "user.address", qualifiedByName = "fromAddress")
    public abstract IndividualDto from(Individual individual);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "updated", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "passportNumber", source = "passportNumber")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "user", expression = "java(userMapper.update(individual, dto))")
    public abstract void update(
        @MappingTarget
        Individual individual,
        IndividualWriteDto dto
    );
}
