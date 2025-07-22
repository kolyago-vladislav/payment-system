package com.example.transaction.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transaction.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

}
