package com.example.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.individual.dto.IndividualDto;
import com.example.individual.dto.IndividualPageDto;
import com.example.individual.dto.WalletDto;
import com.example.individual.dto.WalletPageDto;
import com.example.individual.dto.WalletWriteDto;
import com.example.individual.dto.WalletWriteResponseDto;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface WalletMapper {

    com.example.transaction.dto.WalletWriteDto from(WalletWriteDto dto);
    WalletDto from(com.example.transaction.dto.WalletDto dto);

    com.example.person.dto.IndividualDto from(IndividualDto dto);
    IndividualDto from(com.example.person.dto.IndividualDto dto);

    WalletWriteResponseDto from(com.example.transaction.dto.WalletWriteResponseDto dto);

    List<WalletWriteResponseDto> from(List<com.example.transaction.dto.WalletWriteResponseDto> dto);


    IndividualPageDto from(com.example.person.dto.IndividualPageDto dto);

    WalletPageDto from(com.example.transaction.dto.WalletPageDto dto);
}
