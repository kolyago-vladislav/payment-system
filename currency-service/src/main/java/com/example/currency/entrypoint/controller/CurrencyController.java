package com.example.currency.entrypoint.controller;

import java.time.OffsetDateTime;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.currency.api.CurrencyApi;
import com.example.currency.business.service.CurrencyService;
import com.example.currency.business.service.RateProviderService;
import com.example.currency.business.service.RateService;
import com.example.currency.dto.CurrencyPageDto;
import com.example.currency.dto.RateDto;
import com.example.currency.dto.RateProviderPageDto;

@RestController
@RequiredArgsConstructor
public class CurrencyController implements CurrencyApi {

    private final CurrencyService currencyService;
    private final RateProviderService rateProviderService;
    private final RateService rateService;

    @Override
    public ResponseEntity<CurrencyPageDto> findCurrencies(
        Integer page,
        Integer size
    ) {
        return ResponseEntity.ok(currencyService.loadRates(page, size));
    }

    @Override
    public ResponseEntity<RateDto> findRate(
        String source,
        String target,
        OffsetDateTime timestamp
    ) {
        return ResponseEntity.ok(rateService.findRate(source, target, timestamp));
    }

    @Override
    public ResponseEntity<RateProviderPageDto> findRateProviders(
        Integer page,
        Integer size
    ) {
        return ResponseEntity.ok(rateProviderService.findRateProviders(page, size));
    }
}
