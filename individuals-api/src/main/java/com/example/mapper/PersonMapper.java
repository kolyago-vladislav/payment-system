package com.example.mapper;

import org.mapstruct.Mapper;

import com.example.individual.dto.IndividualDto;
import com.example.individual.dto.IndividualPageDto;
import com.example.individual.dto.IndividualWriteDto;
import com.example.individual.dto.IndividualWriteResponseDto;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface PersonMapper {

    com.example.person.dto.IndividualWriteDto from(IndividualWriteDto dto);

    com.example.person.dto.IndividualDto from(IndividualDto dto);
    IndividualDto from(com.example.person.dto.IndividualDto dto);

    IndividualWriteResponseDto from(com.example.person.dto.IndividualWriteResponseDto dto);

    IndividualPageDto from(com.example.person.dto.IndividualPageDto dto);

    com.example.person.dto.IndividualPageDto from(IndividualPageDto dto);
}
