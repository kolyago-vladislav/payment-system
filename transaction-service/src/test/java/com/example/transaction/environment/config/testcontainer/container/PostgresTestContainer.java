package com.example.transaction.environment.config.testcontainer.container;

import org.testcontainers.containers.PostgreSQLContainer;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;

import static com.example.transaction.environment.config.testcontainer.container.Containers.GLOBAL_NETWORK;

public class PostgresTestContainer {

    public static final PostgreSQLContainer postgresTestContainerShard0;
    public static final PostgreSQLContainer postgresTestContainerShard1;

    static {
        postgresTestContainerShard0 = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("transaction_shard_0")
            .withUsername("postgres")
            .withPassword("postgres")
            .withNetwork(GLOBAL_NETWORK)
            .withNetworkAliases("postgres-0")
            .withExposedPorts(5432)
            .withCreateContainerCmdModifier(cmd ->
                cmd.withPortBindings(new PortBinding(Ports.Binding.bindPort(5433), new ExposedPort(5432)))
            );

        postgresTestContainerShard1 = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("transaction_shard_1")
            .withUsername("postgres")
            .withPassword("postgres")
            .withNetwork(GLOBAL_NETWORK)
            .withNetworkAliases("postgres-1")
            .withExposedPorts(5432)
            .withCreateContainerCmdModifier(cmd ->
                cmd.withPortBindings(new PortBinding(Ports.Binding.bindPort(5435), new ExposedPort(5432)))
            );
    }
}
