package com.example.spec.integration.controller;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.spec.integration.LifecycleSpecification;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionControllerTest extends LifecycleSpecification {

    @Test
    void shouldReturnTransactionConfirmResponseWhenDepositConfirmedCalled() {
        //when
        var request = dtoCreator.transaction.createDepositConfirmTransactionDto();
        var response = transactionApiTestService.confirmDeposit(request);

        //then
        assertEquals(dtoCreator.transaction.createTransactionConfirmResponse(), response);
    }

    @Test
    void shouldReturnTransactionConfirmResponseWhenWithdrawalConfirmedCalled() {
        //when
        var request = dtoCreator.transaction.createWithdrawalConfirmTransactionDto();
        var response = transactionApiTestService.confirmWithdrawal(request);

        //then
        assertEquals(dtoCreator.transaction.createTransactionConfirmResponse(), response);
    }

    @Test
    void shouldReturnTransactionConfirmResponseWhenTransferConfirmedCalled() {
        //when
        var request = dtoCreator.transaction.createTransferConfirmTransactionDto();
        var response = transactionApiTestService.confirmTransfer(request);

        //then
        assertEquals(dtoCreator.transaction.createTransactionConfirmResponse(), response);
    }

    @Test
    void shouldReturnDtoWhenFindByTransactionIdCalled() {
        //when
        var response = transactionApiTestService.findByTransactionId(TRANSACTION_ID);

        //then
        assertEquals(dtoCreator.transaction.createTransactionDto(response.getTransactionId()), response);
    }

    @Test
    void shouldReturnPageDtoWhenFindAllCalled() {
        //when
        var response = transactionApiTestService.findAll(List.of(WALLET_ID));

        //then
        assertEquals(dtoCreator.transaction.createTransactionPageDto(TRANSACTION_ID), response);
    }

}
