package com.example.currency.business.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.currency.business.mapper.RateProviderMapper;
import com.example.currency.business.repository.RateProviderRepository;
import com.example.currency.dto.RateProviderPageDto;

@Service
@RequiredArgsConstructor
public class RateProviderService {

    private final RateProviderRepository repository;
    private final RateProviderMapper mapper;

    public RateProviderPageDto findRateProviders(
        Integer page,
        Integer size
    ) {
        var result = repository.findAll(PageRequest.of(page, size, Sort.Direction.ASC, "id"));

        return new RateProviderPageDto()
            .totalPages(result.getTotalPages())
            .items(mapper.from(result.getContent()));
    }
}
