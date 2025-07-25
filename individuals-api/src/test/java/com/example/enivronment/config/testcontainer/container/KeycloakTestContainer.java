package com.example.enivronment.config.testcontainer.container;

import java.time.Duration;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import com.example.enivronment.config.testcontainer.util.Setting;

import static com.example.enivronment.config.testcontainer.util.Setting.realmInitPath;

public class KeycloakTestContainer {

    public static final GenericContainer keycloakTestContainer;

    static {
        keycloakTestContainer = new GenericContainer<>(DockerImageName.parse("quay.io/keycloak/keycloak:26.2"))
            .dependsOn(Containers.postgres)
            .withExposedPorts(8080)
            .withEnv("KEYCLOAK_ADMIN", "admin")
            .withEnv("KEYCLOAK_ADMIN_PASSWORD", "admin")
            .withEnv("KC_DB", "postgres")
            .withEnv("KC_DB_URL", "jdbc:postgresql://postgres:5432/keycloak")
            .withEnv("KC_DB_USERNAME", "keycloak")
            .withEnv("KC_DB_PASSWORD", "keycloak")
            .withCommand("start-dev --import-realm --health-enabled=true")
            .withNetwork(Setting.GLOBAL_NETWORK)
            .withCopyFileToContainer(MountableFile.forHostPath(realmInitPath()), "/opt/keycloak/data/import/realm-config.json")
            .waitingFor(Wait.forHttp("/"))
            .withStartupTimeout(Duration.ofMinutes(2));
    }

}
