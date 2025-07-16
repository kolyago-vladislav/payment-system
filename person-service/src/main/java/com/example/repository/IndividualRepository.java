package com.example.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.Individual;

public interface IndividualRepository extends JpaRepository<Individual, UUID> {

    @Query("""
        FROM Individual i
            WHERE (:emails IS NULL OR i.user.email IN :emails)
    """)
    List<Individual> findAll(
        @Param("emails")
        List<String> emails
    );

    @Modifying
    @Query("""
        UPDATE Individual i
        SET i.active = false
        WHERE i.id = :id
    """)
    void softDelete(@Param("id") UUID id);

    @Query(
        value = """
                SELECT EXISTS(
                    SELECT email
                    FROM person.users u
                    WHERE u.email = :email
                )
            """, nativeQuery = true
    )
    boolean existsByEmail(String email);

}
