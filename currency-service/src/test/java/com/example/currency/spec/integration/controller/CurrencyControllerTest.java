package com.example.currency.spec.integration.controller;

import org.junit.jupiter.api.Test;

import com.example.currency.spec.LifecycleSpecification;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrencyControllerTest extends LifecycleSpecification {

    @Test
    void shouldReturnDtoWhenFindRateCalled() {
        //given
        loadRateJob.loadRates();

        //when
        var response = currencyApiTestService.findRate();

        //then
        assertEquals(dtoCreator.buildRateDto(), response);
    }

    @Test
    void shouldReturnDtoWhenFindCurrenciesCalled() {
        //when
        var response = currencyApiTestService.findCurrencies();

        //then
        assertEquals(dtoCreator.buildCurrencyPageDto(), response);
    }

    @Test
    void shouldReturnDtoWhenFindRateProvidersCalled() {
        //when
        var response = currencyApiTestService.findRateProviders();

        //then
        assertEquals(dtoCreator.buildRateProviderPageDto(), response);
    }
}
