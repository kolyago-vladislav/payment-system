package com.example.payment.provider.integration.controller;

import org.junit.jupiter.api.Test;

import com.example.payment.provider.integration.LifecycleSpecification;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionControllerTest extends LifecycleSpecification {

    @Test
    void shouldReturnTransactionDto() {
        //when
        var response = service.createTransactionDto();
        var createdPayout = service.findTransactionDtoById(response.getId());

        //then
        assertEquals(dtoCreator.buildTransactionDto(), response);
        assertEquals(dtoCreator.buildTransactionDto(), createdPayout);
    }

}
