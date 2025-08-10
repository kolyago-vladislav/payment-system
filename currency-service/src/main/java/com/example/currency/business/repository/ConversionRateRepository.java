package com.example.currency.business.repository;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.currency.model.entity.ConversionRate;

public interface ConversionRateRepository extends JpaRepository<ConversionRate, Long> {

    @Modifying
    @Query("""
        UPDATE ConversionRate c
        SET c.active = FALSE, c.updatedAt = :now, c.rateEndTime = :now
        WHERE c.active = TRUE AND c.providerId = :providerId
    """)
    void deactivate(String providerId, Instant now);

    ConversionRate findBySourceCodeAndTargetCodeAndActive(String sourceCode, String targetCode, Boolean active);
}
