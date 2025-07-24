package com.example.transaction.business.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transaction.model.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

}
