package com.example.spec.integration.controller;

import org.junit.jupiter.api.Test;

import com.example.spec.integration.LifecycleSpecification;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WalletControllerTest extends LifecycleSpecification {

    @Test
    void shouldRegisterNewWalletAndReturnId() {
        //when
        var response = walletApiTestService.register(dtoCreator.wallet.createWalletWriteDto());

        //then
        assertEquals(WALLET_ID, response);
    }

    @Test
    void shouldReturnDtoWhenFindByIdCalled() {
        //when
        var response = walletApiTestService.findById(WALLET_ID);

        //then
        assertEquals(dtoCreator.wallet.createWalletDto(WALLET_ID), response);
    }

    @Test
    void shouldReturnPageDtoWhenFindByUserIdCalled() {
        //when
        var response = walletApiTestService.findByUserId(PERSON_ID);

        //then
        assertEquals(dtoCreator.wallet.createWalletPageDto(WALLET_ID), response);
    }

}
