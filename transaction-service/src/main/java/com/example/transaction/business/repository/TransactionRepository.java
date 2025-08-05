package com.example.transaction.business.repository;

import jakarta.persistence.LockModeType;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.example.transaction.model.entity.Transaction;
import com.example.transaction.model.entity.type.TransactionStatus;
import com.example.transaction.model.entity.type.TransactionType;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("FROM Transaction t WHERE t.id = :id AND t.status != 'COMPLETED'")
    Optional<Transaction> findByIdForUpdate(UUID id);

    @Query("""
        SELECT t
        FROM Transaction t
        WHERE (:typesIsNull = TRUE OR t.type IN :types)
            AND (:usersIdsIsNull = TRUE OR t.userId IN :usersIds)
            AND (:walletsIdsIsNull = TRUE OR t.walletId IN :walletsIds)
            AND (:statusesIsNull = TRUE OR t.status IN :statuses)
            AND (:fromDateIsNull = TRUE OR t.createdAt >= :fromDate)
            AND (:toDateIsNull = TRUE OR t.createdAt <= :toDate)
    """)
    Page<Transaction> findAllByFilters(
        boolean typesIsNull,
        boolean usersIdsIsNull,
        boolean walletsIdsIsNull,
        boolean statusesIsNull,
        boolean fromDateIsNull,
        boolean toDateIsNull,
        List<TransactionType> types,
        List<String> usersIds,
        List<String> walletsIds,
        List<TransactionStatus> statuses,
        Instant fromDate,
        Instant toDate,
        Pageable pageable
    );
}
