package com.example.transaction.spec.integration.entrypoint.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.transaction.dto.TransactionStatusDto;
import com.example.transaction.model.dto.DepositRequestDto;
import com.example.transaction.model.dto.TransactionPageRequestDto;
import com.example.transaction.model.dto.WithdrawalRequestDto;
import com.example.transaction.spec.integration.NecessaryDependencyConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TransactionControllerTest extends NecessaryDependencyConfig {

    private String walletId;

    @BeforeEach
    void setUp() {
        var walletWriteDto = dtoCreator.wallet.createWalletWriteDto();

        var response = walletApiTestService.register(walletWriteDto);

        walletId = response;

        assertNotNull(response);
    }

    @Test
    void shouldReturnTransactionConfirmResponseWhenDepositConfirmedCalled() {
        //when
        var request = dtoCreator.transaction.createDepositConfirmTransactionDto(walletId);

        var response = transactionApiTestService.confirmDeposit(request);

        var outboxEvent = outboxEventTestService.getOutboxEventByTransactionId(response.getTransactionId());
        var actualDepositEvent = jsonWrapper.read(outboxEvent.getPayload().getBytes(), DepositRequestDto.class);

        var expectedEvent = dtoCreator.outboxEvent.createDepositOutboxEvent(walletId, response.getTransactionId(), outboxEvent.getTraceId());
        var expectedDepositEvent = jsonWrapper.read(expectedEvent.getPayload().getBytes(), DepositRequestDto.class);

        //then
        assertEquals(dtoCreator.transaction.createTransactionConfirmResponse(response.getTransactionId()), response);
        assertEquals(expectedDepositEvent, actualDepositEvent);
    }

    @Test
    void shouldReturnTransactionConfirmResponseWhenWithdrawalConfirmedCalled() {
        //given
        var request = dtoCreator.transaction.createWithdrawalConfirmTransactionDto(walletId);

        //when
        var response = transactionApiTestService.confirmWithdrawal(request);

        var outboxEvent = outboxEventTestService.getOutboxEventByTransactionId(response.getTransactionId());
        var actualWithdrawalEvent = jsonWrapper.read(outboxEvent.getPayload().getBytes(), WithdrawalRequestDto.class);

        var expectedEvent = dtoCreator.outboxEvent.createWithdrawalOutboxEvent(walletId, response.getTransactionId(), outboxEvent.getTraceId());
        var expectedWithdrawalEvent = jsonWrapper.read(expectedEvent.getPayload().getBytes(), WithdrawalRequestDto.class);

        var walletDto = walletApiTestService.findById(walletId);

        //then
        assertEquals(dtoCreator.transaction.createTransactionConfirmResponse(response.getTransactionId()), response);
        assertEquals(expectedWithdrawalEvent, actualWithdrawalEvent);
        assertEquals(-100, walletDto.getBalance());
    }

    @Test
    void shouldReturnTransactionConfirmResponseWhenTransferConfirmedCalled() {
        //given
        var walletWriteDto = dtoCreator.wallet.createWalletWriteDto();
        var secondWalletId = walletApiTestService.register(walletWriteDto);

        var request = dtoCreator.transaction.createTransferConfirmTransactionDto(walletId, secondWalletId);

        //when
        var response = transactionApiTestService.confirmTransfer(request);
        var walletFromDto = walletApiTestService.findById(walletId);
        var walletToDto = walletApiTestService.findById(secondWalletId);

        var expected = dtoCreator.transaction.createTransactionConfirmResponse(response.getTransactionId()).status(TransactionStatusDto.COMPLETED);

        //then
        assertEquals(expected, response);
        assertEquals(-100, walletFromDto.getBalance());
        assertEquals(100, walletToDto.getBalance());
    }

    @Test
    void shouldReturnDtoWhenFindByTransactionIdCalled() {
        //given
        var request = dtoCreator.transaction.createDepositConfirmTransactionDto(walletId);
        var responseId = transactionApiTestService.confirmDeposit(request);

        //when
        var response = transactionApiTestService.findByTransactionId(responseId.getTransactionId());

        //then
        assertEquals(dtoCreator.transaction.createTransactionDto(response.getTransactionId()), response);
    }

    @Test
    void shouldReturnPageDtoWhenFindAllCalled() {
        //given
        var request = dtoCreator.transaction.createDepositConfirmTransactionDto(walletId);
        var first = transactionApiTestService.confirmDeposit(request);
        var second = transactionApiTestService.confirmDeposit(request);

        //when
        var response = transactionApiTestService.findAll(TransactionPageRequestDto.builder().walletIds(List.of(walletId)).build());

        //then
        assertEquals(dtoCreator.transaction.createTransactionPageDto(first.getTransactionId(), second.getTransactionId()), response);
    }

}
