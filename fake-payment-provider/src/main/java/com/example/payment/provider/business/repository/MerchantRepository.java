package com.example.payment.provider.business.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.payment.provider.model.entity.Merchant;

public interface MerchantRepository extends JpaRepository<Merchant, Integer> {

    Optional<Merchant> findByName(String name);
}
