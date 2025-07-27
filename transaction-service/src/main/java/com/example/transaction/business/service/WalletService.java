package com.example.transaction.business.service;

import io.opentelemetry.instrumentation.annotations.WithSpan;
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

    @WithSpan("WalletService.credit")
    @Transactional(isolation = REPEATABLE_READ)
    public void credit(UUID walletId, BigDecimal amount) {
        var wallet = tryGetWalletWithLock(walletId);
        log.debug("Locking wallet is successfully done in database: {}", wallet.getId());
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
        log.info("Wallet balance with id={} was credited", wallet.getId());
    }

    @WithSpan("WalletService.debit")
    @Transactional(isolation = REPEATABLE_READ)
    public void debit(UUID walletId, BigDecimal amount) {
        var wallet = tryGetWalletWithLock(walletId);
        log.debug("Locking wallet is successfully done in database: {}", wallet.getId());
        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);
        log.info("Wallet balance with id={} was debited", wallet.getId());
    }

    private Wallet tryGetWalletWithLock(UUID walletId) {
        try {
            return walletRepository.findByIdForUpdate(walletId);
        } catch (CannotAcquireLockException e) {
            log.warn("Cannot acquire lock for wallet: {}", walletId);
            return tryGetWalletWithLock(walletId);
        }
    }

    @WithSpan("WalletService.create")
    @Transactional
    public WalletWriteResponseDto create(WalletWriteDto walletWriteDto) {
        var wallet = walletMapper.to(walletWriteDto);
        walletRepository.save(wallet);
        log.info("Wallet with id={} was created", wallet.getId());
        return new WalletWriteResponseDto(wallet.getId().toString());
    }

    @WithSpan("WalletService.findById")
    public WalletDto findById(String walletId) {
        var wallet = walletRepository.findById(UUID.fromString(walletId))
            .orElseThrow(() -> new TransactionServiceException("Cannot find wallet with id: %s", walletId));

        return walletMapper.from(wallet);
    }

    @WithSpan("WalletService.findByUserId")
    public WalletPageDto findByUserId(String userId) {
        var wallets = walletRepository.findByUserId(UUID.fromString(userId));

        return new WalletPageDto().items(walletMapper.from(wallets));
    }
}
