package com.example.payment.provider.business.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.example.payment.provider.business.repository.MerchantRepository;
import com.example.payment.provider.model.entity.Merchant;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public Optional<Merchant> findById(String name) {
        return merchantRepository.findByName(name);
    }
}
