package com.example.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.dto.KeycloakCredentialRepresentation;
import com.example.dto.KeycloakUserRepresentation;
import com.example.individual.dto.IndividualWriteDto;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public abstract class KeycloakMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "value", source = "password")
    @Mapping(target = "type", constant = "password")
    @Mapping(target = "temporary", constant = "false")
    public abstract KeycloakCredentialRepresentation toCredentialRepresentation(IndividualWriteDto request);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "username", source = "email")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "emailVerified", constant = "true")
    public abstract KeycloakUserRepresentation toUserRepresentation(IndividualWriteDto request);
}
