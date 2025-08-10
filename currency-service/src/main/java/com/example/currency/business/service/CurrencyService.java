package com.example.currency.business.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.currency.business.mapper.CurrencyMapper;
import com.example.currency.business.repository.CurrencyRepository;
import com.example.currency.dto.CurrencyPageDto;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final RestTemplate restTemplate;
    private final CurrencyRepository repository;
    private final CurrencyMapper mapper;

    public CurrencyPageDto loadRates(Integer page, Integer size) {
        var result = repository.findAll(PageRequest.of(page, size, Sort.Direction.ASC, "isoCode"));

        return new CurrencyPageDto()
            .totalPages(result.getTotalPages())
            .items(mapper.from(result.getContent()));
    }
}
