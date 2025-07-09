package com.example.spec.integration;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.AfterEach;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.KeycloakBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.example.IndividualApiApplication;
import com.example.config.property.KeycloakProperties;
import com.example.enivronment.config.testcontainer.TestcontainersApplicationContextInitializer;
import com.example.enivronment.config.testcontainer.service.IndividualApiService;

@Slf4j
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    classes = IndividualApiApplication.class
)
@ContextConfiguration(initializers = TestcontainersApplicationContextInitializer.class)
@Setter(onMethod_ = {@Autowired})
public abstract class LifecycleSpecification {

    protected IndividualApiService apiService;
    protected KeycloakProperties keycloakProperties;

    @AfterEach
    public void clear() {
        try(var keycloak = KeycloakBuilder.builder()
            .serverUrl(keycloakProperties.serverUrl())
            .realm(keycloakProperties.realm())
            .username(keycloakProperties.adminEmail())
            .password(keycloakProperties.adminPassword())
            .clientId(keycloakProperties.adminClientId())
            .grantType(OAuth2Constants.PASSWORD)
            .build()
        ) {
            var users = keycloak.realm(keycloakProperties.realm()).users().list();

            for (var user : users) {
                if (!keycloakProperties.adminEmail().equals(user.getEmail())) {
                    keycloak.realm(keycloakProperties.realm()).users().delete(user.getId());
                }
            }
        }

    }
}
