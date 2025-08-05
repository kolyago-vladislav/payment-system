package com.example.mapper;

import org.mapstruct.Mapper;

import com.example.individual.dto.ConfirmRequest;
import com.example.individual.dto.DepositConfirmRequest;
import com.example.individual.dto.DepositInitRequest;
import com.example.individual.dto.InitRequest;
import com.example.individual.dto.TransactionConfirmResponse;
import com.example.individual.dto.TransactionDto;
import com.example.individual.dto.TransactionInitResponse;
import com.example.individual.dto.TransactionPageDto;
import com.example.individual.dto.TransactionTypeDto;
import com.example.individual.dto.TransferConfirmRequest;
import com.example.individual.dto.TransferInitRequest;
import com.example.individual.dto.WithdrawalConfirmRequest;
import com.example.individual.dto.WithdrawalInitRequest;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface TransactionMapper {

    com.example.transaction.dto.TransactionTypeDto from(TransactionTypeDto type);


    com.example.transaction.dto.DepositConfirmRequest from(DepositConfirmRequest dto);
    com.example.transaction.dto.TransferConfirmRequest from(TransferConfirmRequest dto);
    com.example.transaction.dto.WithdrawalConfirmRequest from(WithdrawalConfirmRequest dto);

    default com.example.transaction.dto.ConfirmRequest from(TransactionTypeDto type, ConfirmRequest dto) {
        return switch (type) {
            case DEPOSIT -> from((DepositConfirmRequest) dto);
            case TRANSFER -> from((TransferConfirmRequest) dto);
            case WITHDRAWAL -> from((WithdrawalConfirmRequest) dto);
        };
    }

    com.example.transaction.dto.DepositInitRequest from(DepositInitRequest dto);
    com.example.transaction.dto.TransferInitRequest from(TransferInitRequest dto);
    com.example.transaction.dto.WithdrawalInitRequest from(WithdrawalInitRequest dto);

    default com.example.transaction.dto.InitRequest from(TransactionTypeDto type, InitRequest dto) {
        return switch (type) {
            case DEPOSIT -> from((DepositInitRequest) dto);
            case TRANSFER -> from((TransferInitRequest) dto);
            case WITHDRAWAL -> from((WithdrawalInitRequest) dto);
        };
    }

    TransactionConfirmResponse from(com.example.transaction.dto.TransactionConfirmResponse dto);

    TransactionDto from(com.example.transaction.dto.TransactionDto dto);

    TransactionInitResponse from(com.example.transaction.dto.TransactionInitResponse dto);

    TransactionPageDto from(com.example.transaction.dto.TransactionPageDto dto);

}
