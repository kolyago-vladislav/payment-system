package com.example.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.individual.dto.IndividualDto;
import com.example.individual.dto.IndividualWriteDto;
import com.example.individual.dto.IndividualWriteResponseDto;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface PersonMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "confirmPassword", ignore = true)
    com.example.person.dto.IndividualWriteDto from(IndividualWriteDto dto);

    IndividualDto from(com.example.person.dto.IndividualDto dto);

    IndividualWriteResponseDto from(com.example.person.dto.IndividualWriteResponseDto dto);

}
