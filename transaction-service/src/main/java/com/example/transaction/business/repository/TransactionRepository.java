package com.example.transaction.business.repository;

import jakarta.persistence.LockModeType;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.example.transaction.model.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("FROM Transaction t WHERE t.id = :id AND t.status != 'COMPLETED'")
    Optional<Transaction> findByIdForUpdate(UUID id);

    @Query(value = """
        SELECT t.*
        FROM transaction.transactions t
        WHERE (CARDINALITY(:types) = 0 OR t.type = ANY(CAST(:types AS transaction.transaction_type[])))
            AND (CARDINALITY(:userIds) = 0 OR t.user_id = ANY(:userIds))
            AND (CARDINALITY(:walletIds) = 0 OR t.wallet_id = ANY(:walletIds))
            AND (CARDINALITY(:statuses) = 0 OR t.status = ANY(CAST(:statuses AS transaction.transaction_status[])))
            AND (COALESCE(:dateFrom) IS NULL OR t.created_at >= CAST(:dateFrom AS TIMESTAMP))
            AND (COALESCE(:dateTo) IS NULL OR t.created_at <= CAST(:dateTo AS TIMESTAMP))
        ORDER BY t.created_at DESC
        LIMIT :limit
        OFFSET :offset
    """, nativeQuery = true)
    List<Transaction> findAllByFilters(
        UUID[] userIds,
        UUID[] walletIds,
        String[] types,
        String[] statuses,
        OffsetDateTime dateFrom,
        OffsetDateTime dateTo,
        int limit,
        int offset
    );
}
