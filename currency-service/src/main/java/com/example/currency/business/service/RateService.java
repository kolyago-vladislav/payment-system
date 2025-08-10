package com.example.currency.business.service;

import java.time.OffsetDateTime;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.example.currency.business.mapper.ConversionRateMapper;
import com.example.currency.business.repository.ConversionRateRepository;
import com.example.currency.core.util.DateTimeUtil;
import com.example.currency.dto.RateDto;

@Service
@RequiredArgsConstructor
public class RateService {

    private final ConversionRateRepository repository;
    private final ConversionRateMapper mapper;
    private final DateTimeUtil dateTimeUtil;

    public RateDto findRate(
        String from,
        String to,
        OffsetDateTime timestamp
    ) {
        var latestRate = repository.findBySourceCodeAndTargetCodeAndActive(from, to, true);
        return mapper.from(latestRate);
    }
}
