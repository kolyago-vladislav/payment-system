package com.example.transaction.entrypoint.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.transaction.api.WalletApi;
import com.example.transaction.business.service.WalletService;
import com.example.transaction.dto.WalletDto;
import com.example.transaction.dto.WalletPageDto;
import com.example.transaction.dto.WalletWriteDto;
import com.example.transaction.dto.WalletWriteResponseDto;

@RestController
@RequiredArgsConstructor
public class WalletController implements WalletApi {

    private final WalletService walletService;

    @Override
    public ResponseEntity<WalletWriteResponseDto> create(WalletWriteDto walletWriteDto) {
        return ResponseEntity.ok(walletService.create(walletWriteDto));
    }

    @Override
    public ResponseEntity<WalletDto> findById(String walletId) {
        return ResponseEntity.ok(walletService.findById(walletId));
    }

    @Override
    public ResponseEntity<WalletPageDto> findByUserId(String userId) {
        return ResponseEntity.ok(walletService.findByUserId(userId));
    }
}
