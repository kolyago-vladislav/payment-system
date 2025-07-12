package com.example.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.Individual;

public interface IndividualRepository extends JpaRepository<Individual, UUID> {

    @Query("""
        FROM Individual i
        WHERE i.user.email = :email
    """)
    Optional<Individual> findByEmail(String email);

    @Modifying
    @Query("""
        UPDATE Individual i
        SET i.active = false
        WHERE i.id = :id
    """)
    void softDelete(@Param("id") UUID id);
}
