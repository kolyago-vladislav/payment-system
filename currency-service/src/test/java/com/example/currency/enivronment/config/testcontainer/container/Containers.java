package com.example.currency.enivronment.config.testcontainer.container;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.wiremock.integrations.testcontainers.WireMockContainer;

@Slf4j
@UtilityClass
public class Containers {

    public static final Network GLOBAL_NETWORK = Network.newNetwork();

    public PostgreSQLContainer postgres = PostgresTestContainer.postgresTestContainer;
    public WireMockContainer wireMockContainer = WireMockTestContainer.wireMockContainer;

    public void run() {
        postgres.start();
        wireMockContainer.start();
    }
}
