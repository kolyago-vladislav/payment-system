package com.example.transaction.business.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transaction.model.entity.WalletType;

public interface WalletTypeRepository extends JpaRepository<WalletType, UUID> {

}
