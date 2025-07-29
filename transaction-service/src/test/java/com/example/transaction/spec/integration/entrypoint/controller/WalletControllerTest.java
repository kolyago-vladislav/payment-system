package com.example.transaction.spec.integration.entrypoint.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.transaction.spec.integration.NecessaryDependencyConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WalletControllerTest extends NecessaryDependencyConfig {

    private String id;

    @BeforeEach
    void setUp() {
        var walletWriteDto = dtoCreator.wallet.createWalletWriteDto();

        var response = walletApiTestService.register(walletWriteDto);

        id = response;

        assertNotNull(response);
    }

    @Test
    void shouldReturnDtoWhenFindByIdCalled() {
        //when
        var response = walletApiTestService.findById(id);

        //then
        assertEquals(dtoCreator.wallet.createWalletDto(id), response);
    }

    @Test
    void shouldReturnPageDtoWhenFindByUserIdCalled() {
        //when
        var response = walletApiTestService.findByUserId(USER_ID);

        //then
        assertEquals(dtoCreator.wallet.createWalletPageDto(id), response);
    }

}
