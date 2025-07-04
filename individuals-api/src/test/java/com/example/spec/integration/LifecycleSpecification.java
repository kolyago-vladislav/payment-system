package com.example.spec.integration;

import lombok.Setter;

import org.junit.jupiter.api.AfterEach;
import org.keycloak.admin.client.Keycloak;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.example.IndividualApiApplication;
import com.example.config.property.KeycloakProperties;
import com.example.enivronment.config.testcontainer.TestcontainersApplicationContextInitializer;
import com.example.enivronment.config.testcontainer.service.IndividualApiService;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    classes = IndividualApiApplication.class
)
@ContextConfiguration(initializers = TestcontainersApplicationContextInitializer.class)
@Setter(onMethod_ = {@Autowired})
public abstract class LifecycleSpecification {

    protected Keycloak keycloak;
    protected IndividualApiService apiService;
    protected KeycloakProperties keycloakProperties;

    @AfterEach
    void setUp() {
        var users = keycloak.realm(keycloakProperties.realm()).users().list();

        for (var user : users) {
            if (!keycloakProperties.adminUsername().equals(user.getUsername())) {
                keycloak.realm(keycloakProperties.realm()).users().delete(user.getId());
            }
        }
    }
}
