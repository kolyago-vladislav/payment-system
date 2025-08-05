package com.example.service;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import com.example.individual.dto.WalletDto;
import com.example.individual.dto.WalletPageDto;
import com.example.individual.dto.WalletWriteDto;
import com.example.individual.dto.WalletWriteResponseDto;
import com.example.mapper.WalletMapper;
import com.example.transaction.api.WalletApiClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletApiClient walletApiClient;
    private final WalletMapper walletMapper;

    @WithSpan(value = "walletService.findById")
    public Mono<WalletDto> findById(UUID id) {
        return Mono.fromCallable(() -> walletApiClient.findById(id.toString()))
            .map(dto -> walletMapper.from(dto.getBody()))
            .subscribeOn(Schedulers.boundedElastic());
    }

    @WithSpan(value = "walletService.findByUserId")
    public Mono<WalletPageDto> findByUserId(UUID id) {
        return Mono.fromCallable(() -> walletApiClient.findByUserId(id.toString()))
            .map(dto -> walletMapper.from(dto.getBody()))
            .subscribeOn(Schedulers.boundedElastic());
    }

    @WithSpan("walletService.register")
    public Mono<WalletWriteResponseDto> register(Mono<WalletWriteDto> request) {
        return request
            .map(walletMapper::from)
            .flatMap(dto ->
                Mono.fromCallable(() -> walletApiClient.create(dto))
                    .mapNotNull(HttpEntity::getBody)
                    .map(walletMapper::from)
                    .subscribeOn(Schedulers.boundedElastic())
            )
            .doOnNext(t -> log.info("Wallet registered id={}", t.getId()));
    }

}
