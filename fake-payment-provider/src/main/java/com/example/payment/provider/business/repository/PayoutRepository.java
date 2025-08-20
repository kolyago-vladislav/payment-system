package com.example.payment.provider.business.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.payment.provider.model.entity.Payout;

public interface PayoutRepository extends JpaRepository<Payout, Long> {

    @Query(
        value = """
                SELECT *
                FROM payment_provider.payouts
                WHERE status = 'PENDING' AND updated_at <= NOW() AT TIME ZONE 'UTC'
                LIMIT 1
                FOR UPDATE SKIP LOCKED
            """, nativeQuery = true
    )
    Optional<Payout> findByPendingStatus();
}
