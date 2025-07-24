package com.example.transaction.business.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.transaction.business.repository.WalletRepository;
import com.example.transaction.model.entity.Transaction;
import com.example.transaction.model.entity.Wallet;

import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    @Transactional(isolation = REPEATABLE_READ)
    public void deposit(Transaction transaction) {
        var wallet = tryGetWalletWithLock(transaction);
        wallet.setBalance(wallet.getBalance().add(transaction.getAmount()));
        walletRepository.save(wallet);
    }

    private Wallet tryGetWalletWithLock(Transaction transaction) {
        try {
            return walletRepository.findByIdForUpdate(transaction.getWallet().getId());
        } catch (CannotAcquireLockException e) {
            log.error("Cannot acquire lock for wallet: {}", transaction.getWallet().getId());
            return tryGetWalletWithLock(transaction);
        }
    }
}
