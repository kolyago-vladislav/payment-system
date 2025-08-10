package com.example.currency.business.client;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.currency.core.util.JsonWrapper;
import com.example.currency.model.dto.CurrencyRateDto;
import com.example.currency.model.entity.Currency;

@Component
public class FrankfurterClient {

    private final JsonWrapper jsonWrapper;
    private final RestTemplate restTemplate;
    private final String url;

    public FrankfurterClient(
        JsonWrapper jsonWrapper,
        RestTemplate restTemplate,
        @Value("${client.frankfurter.url}")
        String url
    ) {
        this.jsonWrapper = jsonWrapper;
        this.restTemplate = restTemplate;
        this.url = url;
    }

    public Map<Currency, CurrencyRateDto> loadLatestRates(List<Currency> currencies) {
        var rates = new HashMap<Currency, CurrencyRateDto>();
        for (var currency : currencies) {
            var response = restTemplate.getForObject(url + "/latest?base=" + currency.getCode(), String.class);
            rates.put(currency, jsonWrapper.read(response, CurrencyRateDto.class));
        }

        return rates;
    }
}
