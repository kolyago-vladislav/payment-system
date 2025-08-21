package com.example.payment.provider.integration.controller;

import org.junit.jupiter.api.Test;

import com.example.payment.provider.integration.LifecycleSpecification;
import com.example.payment.provider.model.dto.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WebhookControllerTest extends LifecycleSpecification {

    @Test
    void shouldReturnPayoutDto() {
        //when
        var response = service.createPayout();
        service.updatePayoutStatus(response.getId());
        var updatedPayout = service.findPayoutDtoById(response.getId());

        //then
        var payoutDto = dtoCreator.buildPayoutDto();
        payoutDto.setStatus(Status.SUCCESS.name());
        assertEquals(payoutDto, updatedPayout);
    }

    @Test
    void shouldReturnTransactionDto() {
        //when
        var response = service.createTransactionDto();
        service.updateTransactionStatus(response.getId());
        var updatedPayout = service.findTransactionDtoById(response.getId());

        //then
        var dto = dtoCreator.buildTransactionDto();
        dto.setStatus(Status.SUCCESS.name());
        assertEquals(dto, updatedPayout);
    }

}
