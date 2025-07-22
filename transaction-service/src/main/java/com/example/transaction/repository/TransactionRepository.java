package com.example.transaction.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transaction.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

}
