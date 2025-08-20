package com.example.payment.provider.business.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.payment.provider.business.mapper.PayoutMapper;
import com.example.payment.provider.business.repository.PayoutRepository;
import com.example.payment.provider.core.exception.PaymentProviderException;
import com.example.payment.provider.dto.PayoutDto;
import com.example.payment.provider.dto.PayoutWriteDto;
import com.example.payment.provider.dto.StatusUpdateDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayoutService {

    private final PayoutMapper mapper;
    private final PayoutRepository repository;

    @Transactional
    public PayoutDto createPayout(PayoutWriteDto dto) {
        var payout = mapper.to(dto);
        repository.save(payout);

        log.info("Payout was successfully created id={}", payout.getId());

        return mapper.from(payout);
    }

    public PayoutDto findByPayoutId(Long id) {
        var payout = repository.findById(id)
            .orElseThrow(() -> new PaymentProviderException("Payout not found by id: %s", id));

        log.info("Payout was successfully found id={}", payout.getId());

        return mapper.from(payout);
    }

    public void updateStatus(StatusUpdateDto statusUpdateDto) {
        var payout = repository.findById(statusUpdateDto.getId())
            .orElseThrow(() -> new PaymentProviderException("Payout not found by id: %s", statusUpdateDto.getId()));

        payout.setStatus(statusUpdateDto.getStatus());

        repository.save(payout);
    }
}
