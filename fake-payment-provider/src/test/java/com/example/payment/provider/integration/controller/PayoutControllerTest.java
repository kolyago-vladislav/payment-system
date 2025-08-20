package com.example.payment.provider.integration.controller;

import org.junit.jupiter.api.Test;

import com.example.payment.provider.integration.LifecycleSpecification;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PayoutControllerTest extends LifecycleSpecification {

    @Test
    void shouldReturnPayoutDto() {
        //when
        var response = service.createPayout();
        var createdPayout = service.findPayoutDtoById(response.getId());

        //then
        assertEquals(dtoCreator.buildPayoutDto(), response);
        assertEquals(dtoCreator.buildPayoutDto(), createdPayout);
    }

}
