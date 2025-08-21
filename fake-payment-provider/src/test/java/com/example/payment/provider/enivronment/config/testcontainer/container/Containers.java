package com.example.payment.provider.enivronment.config.testcontainer.container;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;

@Slf4j
@UtilityClass
public class Containers {

    public static final Network GLOBAL_NETWORK = Network.newNetwork();

    public PostgreSQLContainer postgres = PostgresTestContainer.postgresTestContainer;

    public void run() {
        postgres.start();
    }
}
