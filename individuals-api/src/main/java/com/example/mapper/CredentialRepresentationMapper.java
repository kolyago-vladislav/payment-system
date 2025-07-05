package com.example.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.dto.KeycloakCredentialDto;
import com.example.dto.UserRegistrationRequest;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public abstract class CredentialRepresentationMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "value", source = "password")
    @Mapping(target = "type", constant = "password")
    @Mapping(target = "temporary", constant = "false")
    public abstract KeycloakCredentialDto to(UserRegistrationRequest request);
}
