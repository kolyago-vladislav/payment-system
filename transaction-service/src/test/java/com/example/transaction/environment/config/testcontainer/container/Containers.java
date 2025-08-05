package com.example.transaction.environment.config.testcontainer.container;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@Slf4j
@UtilityClass
public class Containers {

    public static final Network GLOBAL_NETWORK = Network.newNetwork();

    public PostgreSQLContainer postgresShard0 = PostgresTestContainer.postgresTestContainerShard0;
    public PostgreSQLContainer postgresShard1 = PostgresTestContainer.postgresTestContainerShard1;
    public GenericContainer flywayContainer0 = FlywayTestContainer.flywayContainer0;
    public GenericContainer flywayContainer1 = FlywayTestContainer.flywayContainer1;

    public void run() {
        postgresShard0.start();
        flywayContainer0
            .dependsOn(postgresShard0)
            .waitingFor(
                Wait.forLogMessage(".*(Successfully applied).*", 1)
            );

        flywayContainer0.start();

        postgresShard1.start();
        flywayContainer1
            .dependsOn(postgresShard1)
            .waitingFor(
                Wait.forLogMessage(".*(Successfully applied).*", 1)
            );

        flywayContainer1.start();
    }
}
