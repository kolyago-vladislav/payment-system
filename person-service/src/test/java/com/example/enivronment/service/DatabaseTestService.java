package com.example.enivronment.service;

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
        "person.addresses",
        "person.individuals",
        "person.users",
        "person_history.addresses_history",
        "person_history.individuals_history",
        "person_history.users_history"
    );
}
