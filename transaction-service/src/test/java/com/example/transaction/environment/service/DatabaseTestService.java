package com.example.transaction.environment.service;

import java.sql.PreparedStatement;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseTestService {

    public static final String CASCADE_TRUNCATE = """
            TRUNCATE TABLE %s CONTINUE IDENTITY CASCADE
        """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void truncateAllTables() {
        TABLES_NAMES.forEach(table -> jdbcTemplate.execute(CASCADE_TRUNCATE.formatted(table), PreparedStatement::execute));
    }

    public static final List<String> TABLES_NAMES = List.of(
        "transaction.wallets",
        "transaction.transactions",
        "transaction.outbox_events"
    );
}
