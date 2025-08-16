package com.example.currency.enivronment.data;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.currency.dto.CurrencyDto;
import com.example.currency.dto.CurrencyPageDto;
import com.example.currency.dto.RateDto;
import com.example.currency.dto.RateProviderDto;
import com.example.currency.dto.RateProviderPageDto;

@Component
public class DtoCreator {

    public RateDto buildRateDto() {
        return new RateDto()
            .providerCode("FRANKFURTER")
            .source("USD")
            .target("EUR")
            .rate(BigDecimal.valueOf(0.96411796));
    }

    public CurrencyPageDto buildCurrencyPageDto() {
        return new CurrencyPageDto()
            .totalPages(16)
            .items(List.of(buildAudCurrencyDto(), buildCadCurrencyDto()));
    }

    public CurrencyDto buildAudCurrencyDto() {
        return new CurrencyDto()
            .active(true)
            .code("AUD")
            .isoCode(36)
            .description("Australian Dollar")
            .symbol("$");
    }

    public CurrencyDto buildCadCurrencyDto() {
        return new CurrencyDto()
            .active(true)
            .code("CAD")
            .isoCode(124)
            .description("Canadian Dollar")
            .symbol("$");
    }

    public RateProviderPageDto buildRateProviderPageDto() {
        return new RateProviderPageDto()
            .totalPages(1)
            .items(List.of(buildRateProviderDto()));
    }

    public RateProviderDto buildRateProviderDto() {
        return new RateProviderDto()
            .active(true)
            .code("FRANKFURTER")
            .name("Frankfurter")
            .priority(1)
            .description("Free and open-source currency rates API by the European Central Bank (https://frankfurter.dev/)");
    }
}
