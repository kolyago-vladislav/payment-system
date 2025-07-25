package com.example.transaction.business.service;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.transaction.business.mapper.WalletMapper;
import com.example.transaction.business.repository.WalletRepository;
import com.example.transaction.core.exception.TransactionServiceException;
import com.example.transaction.dto.WalletDto;
import com.example.transaction.dto.WalletPageDto;
import com.example.transaction.dto.WalletWriteDto;
import com.example.transaction.dto.WalletWriteResponseDto;
import com.example.transaction.model.entity.Wallet;

import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    @Transactional(isolation = REPEATABLE_READ)
    public void credit(UUID walletId, BigDecimal amount) {
        var wallet = tryGetWalletWithLock(walletId);
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
    }

    @Transactional(isolation = REPEATABLE_READ)
    public void debit(UUID walletId, BigDecimal amount) {
        var wallet = tryGetWalletWithLock(walletId);
        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);
    }

    private Wallet tryGetWalletWithLock(UUID walletId) {
        try {
            return walletRepository.findByIdForUpdate(walletId);
        } catch (CannotAcquireLockException e) {
            log.warn("Cannot acquire lock for wallet: {}", walletId);
            return tryGetWalletWithLock(walletId);
        }
    }

    @Transactional
    public WalletWriteResponseDto create(WalletWriteDto walletWriteDto) {
        var wallet = walletMapper.to(walletWriteDto);
        walletRepository.save(wallet);
        return new WalletWriteResponseDto(wallet.getId().toString());
    }

    public WalletDto findById(String walletId) {
        var wallet = walletRepository.findById(UUID.fromString(walletId))
            .orElseThrow(() -> new TransactionServiceException("Cannot find wallet with id: %s", walletId));

        return walletMapper.from(wallet);
    }

    public WalletPageDto findByUserId(String userId) {
        var wallets = walletRepository.findByUserId(UUID.fromString(userId));

        return new WalletPageDto().items(walletMapper.from(wallets));
    }
}
