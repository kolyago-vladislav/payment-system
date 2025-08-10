package com.example.enivronment.config.testcontainer.container;

import org.wiremock.integrations.testcontainers.WireMockContainer;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;

public class WireMockTestContainer {

    public static WireMockContainer wireMockContainer;

    static {
        wireMockContainer = new WireMockContainer("wiremock/wiremock:3.13.0")
            .withCreateContainerCmdModifier(cmd -> cmd.withPortBindings(
                new PortBinding(Ports.Binding.bindPort(8092), new ExposedPort(8080)),
                new PortBinding(Ports.Binding.bindPort(8093), new ExposedPort(8080)),
                new PortBinding(Ports.Binding.bindPort(8094), new ExposedPort(8080))
            ))
            .withMappingFromResource("hackernews", "mappings/stubs.json");
    }
}
