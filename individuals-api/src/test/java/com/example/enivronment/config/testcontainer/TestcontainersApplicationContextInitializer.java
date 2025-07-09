package com.example.enivronment.config.testcontainer;

import java.util.Map;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;

import com.example.enivronment.config.testcontainer.container.Containers;
import com.example.enivronment.config.testcontainer.container.KeycloakTestContainer;

public class TestcontainersApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Containers.run();
        var newSource = new MapPropertySource("override-props", Map.of("application.keycloak.serverUrl", buildServerUrl()));
        applicationContext.getEnvironment().getPropertySources().addFirst(newSource);
    }

    private static String buildServerUrl() {
        return "http://"
               + KeycloakTestContainer.keycloakTestContainer.getHost()
               + ":"
               + KeycloakTestContainer.keycloakTestContainer.getMappedPort(8080);
    }
}
