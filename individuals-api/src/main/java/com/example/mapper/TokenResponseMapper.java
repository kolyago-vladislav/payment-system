package com.example.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.dto.KeycloakAccessTokenResponse;
import com.example.dto.KeycloakCredentialRepresentation;
import com.example.dto.KeycloakUserRepresentation;
import com.example.dto.TokenResponse;
import com.example.dto.UserRegistrationRequest;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public abstract class TokenResponseMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "accessToken", source = "token")
    @Mapping(target = "refreshToken", source = "refreshToken")
    @Mapping(target = "expiresIn", source = "expiresIn")
    @Mapping(target = "tokenType", source = "tokenType")
    public abstract TokenResponse toTokenResponse(KeycloakAccessTokenResponse request);

}
