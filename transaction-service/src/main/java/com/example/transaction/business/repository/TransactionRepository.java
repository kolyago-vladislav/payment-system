package com.example.transaction.business.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transaction.model.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

}
