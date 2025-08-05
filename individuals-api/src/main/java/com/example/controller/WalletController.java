package com.example.controller;

import reactor.core.publisher.Mono;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.example.individual.api.WalletApi;
import com.example.individual.dto.WalletDto;
import com.example.individual.dto.WalletPageDto;
import com.example.individual.dto.WalletWriteDto;
import com.example.individual.dto.WalletWriteResponseDto;
import com.example.service.WalletService;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class WalletController implements WalletApi {

    private final WalletService walletService;

    @Override
    public Mono<ResponseEntity<WalletWriteResponseDto>> create(
        Mono<WalletWriteDto> walletWriteDto,
        ServerWebExchange exchange
    ) {
        return walletService
            .register(walletWriteDto)
            .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<WalletDto>> findById(
        String walletId,
        ServerWebExchange exchange
    ) {
        return walletService
            .findById(UUID.fromString(walletId))
            .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<WalletPageDto>> findByUserId(
        String userId,
        ServerWebExchange exchange
    ) {
        return walletService
            .findByUserId(UUID.fromString(userId))
            .map(ResponseEntity::ok);
    }
}
