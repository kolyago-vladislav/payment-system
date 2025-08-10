package com.example.currency.enivronment.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.currency.dto.CurrencyPageDto;
import com.example.currency.dto.RateDto;
import com.example.currency.dto.RateProviderPageDto;

@Component
public class CurrencyApiTestService {

    private final RestTemplate restTemplate;
    private final String url;

    public CurrencyApiTestService(
        RestTemplate restTemplate,
        @Value("${server.port}")
        int port
    ) {
        this.restTemplate = restTemplate;
        this.url = "http://localhost:" + port + "/currency";
    }

    public RateDto findRate() {
        var path = url + "/v1/rates?source" + "=USD" + "&target=EUR";
        return restTemplate.getForObject(path, RateDto.class);
    }

    public CurrencyPageDto findCurrencies() {
        var path = url + "/v1/currencies?page" + "=0" + "&size=2";
        return restTemplate.getForObject(path, CurrencyPageDto.class);
    }

    public RateProviderPageDto findRateProviders() {
        var path = url + "/v1/rate-providers?page" + "=0" + "&size=2";
        return restTemplate.getForObject(path, RateProviderPageDto.class);
    }

}
