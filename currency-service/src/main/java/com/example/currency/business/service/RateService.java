package com.example.currency.business.service;

import java.time.OffsetDateTime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.example.currency.business.mapper.ConversionRateMapper;
import com.example.currency.business.repository.ConversionRateRepository;
import com.example.currency.core.exception.CurrencyServiceException;
import com.example.currency.dto.RateDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateService {

    private final ConversionRateRepository repository;
    private final ConversionRateMapper mapper;

    public RateDto findRate(
        String from,
        String to,
        OffsetDateTime timestamp
    ) {
        var latestRate = repository.findBySourceCodeAndTargetCodeAndActive(from, to, true)
            .orElseThrow(() -> new CurrencyServiceException("Can not find rate for %s to %s", from, to));
        log.info("Rate found: source={}, target={}, rate={}", latestRate.getSourceCode(), latestRate.getTargetCode(), latestRate.getRate());
        return mapper.from(latestRate);
    }
}
