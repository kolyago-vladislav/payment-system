package com.example.transaction.business.service;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.transaction.business.repository.WalletRepository;
import com.example.transaction.model.entity.Wallet;

import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

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
}
