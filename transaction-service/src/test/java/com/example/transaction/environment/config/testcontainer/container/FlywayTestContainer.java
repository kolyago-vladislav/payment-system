package com.example.transaction.environment.config.testcontainer.container;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;

import static com.example.transaction.environment.config.testcontainer.container.Containers.GLOBAL_NETWORK;

public class FlywayTestContainer {

    public static final GenericContainer flywayContainer0;
    public static final GenericContainer flywayContainer1;

    static {
        Path flywayPath = Paths.get("../databases/transaction").toAbsolutePath().normalize();

        flywayContainer0 = flyway(flywayPath, "jdbc:postgresql://postgres-0:5432/transaction_shard_0");
        flywayContainer1 = flyway(flywayPath, "jdbc:postgresql://postgres-1:5432/transaction_shard_1");
    }

    private static GenericContainer flyway(Path flywayPath, String jdbcUrl) {
        return new GenericContainer<>("flyway/flyway")
            .withNetwork(GLOBAL_NETWORK)
            .withNetworkAliases("flyway-transaction")
            .withEnv("FLYWAY_URL", jdbcUrl)
            .withEnv("FLYWAY_USER", "postgres")
            .withEnv("FLYWAY_PASSWORD", "postgres")
            .withEnv("FLYWAY_LOCATIONS", "filesystem:/flyway/sql")
            .withFileSystemBind(flywayPath.toString(), "/flyway/sql", BindMode.READ_ONLY)
            .withCommand("migrate");
    }
}
