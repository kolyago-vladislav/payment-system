package com.example.payment.provider.enivronment.config.testcontainer.container;

import org.testcontainers.containers.PostgreSQLContainer;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;

import static com.example.payment.provider.enivronment.config.testcontainer.container.Containers.GLOBAL_NETWORK;

public class PostgresTestContainer {

    public static final PostgreSQLContainer postgresTestContainer;

    static {
        postgresTestContainer = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("payment_provider")
            .withUsername("postgres")
            .withPassword("postgres")
            .withNetwork(GLOBAL_NETWORK)
            .withNetworkAliases("postgres")
            .withExposedPorts(5432)
            .withCreateContainerCmdModifier(cmd ->
                cmd.withPortBindings(new PortBinding(Ports.Binding.bindPort(5434), new ExposedPort(5432)))
            );
    }
}
