package com.example.currency.business.service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.currency.business.client.FrankfurterClient;
import com.example.currency.business.mapper.ConversionRateMapper;
import com.example.currency.business.repository.ConversionRateRepository;
import com.example.currency.business.repository.CurrencyRepository;
import com.example.currency.model.dto.CurrencyRateDto;
import com.example.currency.model.entity.ConversionRate;
import com.example.currency.model.entity.Currency;

@Slf4j
@Service
public class FrankfurterRateLoader {

    private final FrankfurterClient client;
    private final CurrencyRepository currencyRepository;
    private final ConversionRateMapper mapper;
    private final ConversionRateRepository conversionRateRepository;
    private final Clock clock;
    private final BigDecimal correctionFactor;

    public FrankfurterRateLoader(
        FrankfurterClient client,
        CurrencyRepository currencyRepository,
        ConversionRateMapper mapper,
        ConversionRateRepository conversionRateRepository,
        Clock clock,
        @Value("${client.frankfurter.correctionFactor}")
        BigDecimal correctionFactor
    ) {
        this.client = client;
        this.currencyRepository = currencyRepository;
        this.mapper = mapper;
        this.conversionRateRepository = conversionRateRepository;
        this.clock = clock;
        this.correctionFactor = correctionFactor;
    }

    @Transactional
    public void loadRates() {
        var exchangeRates = client.loadLatestRates(currencyRepository.findAll());

        conversionRateRepository.deactivate(getProviderId(), LocalDateTime.now(clock).toInstant(ZoneOffset.UTC));

        exchangeRates.forEach(this::saveRates);

        log.info("Rates count={} updated", exchangeRates.size());
    }

    private void saveRates(
        Currency sourceCurrency,
        CurrencyRateDto rateDto
    ) {
        var conversionRates = rateDto.rates().entrySet().stream()
            .map(entry -> toEntity(sourceCurrency, entry, rateDto.amount()))
            .toList();

        conversionRateRepository.saveAll(conversionRates);
    }

    private ConversionRate toEntity(
        Currency sourceCurrency,
        Map.Entry<String, BigDecimal> entry,
        Integer amount
    ) {
        return mapper.toEntity(
            sourceCurrency.getCode(),
            entry.getKey(),
            correctionFactor.multiply(entry.getValue()),
            amount,
            LocalDateTime.now(clock).toInstant(ZoneOffset.UTC),
            getProviderId()
        );
    }

    public String getProviderId() {
        return "FRANKFURTER";
    }
}
