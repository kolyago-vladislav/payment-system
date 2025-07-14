package com.example.enivronment.config.testcontainer.container;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;

import static com.example.enivronment.config.testcontainer.container.Containers.GLOBAL_NETWORK;

public class FlywayTestContainer {

    public static final GenericContainer flywayContainer;

    static {
        Path flywayPath = Paths.get("../flyway").toAbsolutePath().normalize();

        flywayContainer =
            new GenericContainer<>("flyway/flyway")
                .withNetwork(GLOBAL_NETWORK)
                .withNetworkAliases("flyway-persons")
                .withEnv("FLYWAY_URL", "jdbc:postgresql://postgres:5432/person")
                .withEnv("FLYWAY_USER", "postgres")
                .withEnv("FLYWAY_PASSWORD", "postgres")
                .withEnv("FLYWAY_LOCATIONS", "filesystem:/flyway/sql")
                .withFileSystemBind(flywayPath.toString(), "/flyway/sql", BindMode.READ_ONLY)
                .withCommand("migrate");
    }
}
