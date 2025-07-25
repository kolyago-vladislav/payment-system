package com.example.transaction.business.service.transaction.confirm;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.transaction.business.mapper.TransactionMapper;
import com.example.transaction.business.repository.TransactionRepository;
import com.example.transaction.business.service.WalletService;
import com.example.transaction.business.service.transaction.confirm.base.ConfirmRequestHandler;
import com.example.transaction.dto.ConfirmRequest;
import com.example.transaction.dto.TransactionConfirmResponse;
import com.example.transaction.dto.TransferConfirmRequest;
import com.example.transaction.model.entity.type.TransactionType;

import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;

import static com.example.transaction.model.entity.type.TransactionType.TRANSFER;

@Component
@RequiredArgsConstructor
public class TransferConfirmHandler implements ConfirmRequestHandler {

    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final WalletService walletService;

    @Override
    @Transactional(isolation = REPEATABLE_READ)
    public TransactionConfirmResponse handle(ConfirmRequest confirmRequest) {
        var request = (TransferConfirmRequest) confirmRequest;
        var transaction = transactionRepository.save(transactionMapper.to(request, TRANSFER));
        walletService.debit(transaction.getWallet().getId(), transaction.getAmount());
        walletService.credit(transaction.getTargetWallet().getId(), transaction.getAmount());

        return transactionMapper.from(transaction);
    }

    public TransactionType getType() {
        return TransactionType.TRANSFER;
    }
}