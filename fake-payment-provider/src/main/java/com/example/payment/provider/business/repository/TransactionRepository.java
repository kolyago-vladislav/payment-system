package com.example.payment.provider.business.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.payment.provider.model.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(
        value = """
                SELECT *
                FROM payment_provider.transactions
                WHERE status = 'PENDING' AND updated_at <= now() AT TIME ZONE 'UTC'
                LIMIT 1
                FOR UPDATE SKIP LOCKED
            """, nativeQuery = true
    )
    Optional<Transaction> findByPendingStatus();
}
