package com.example.enivronment.config.testcontainer.service;

import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.keycloak.admin.client.Keycloak;

import org.springframework.stereotype.Service;

import com.example.config.property.KeycloakProperties;
import com.example.exception.IndividualException;
import com.example.individual.dto.UserLoginRequest;

@Service
@RequiredArgsConstructor
public class KeycloakApiTestService {

    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;
    private final IndividualApiTestService individualApiTestService;

    public Map<String, List<String>> getUserAttributes(String email) {
        var users = keycloak.realm(keycloakProperties.realm()).users().list();

        for (var user : users) {
            if (user.getEmail().equals(email)) {
                return user.getAttributes();
            }
        }

        throw new IndividualException("User not found by email=[ %s ]", email);
    }

    public void clear() {
        var users = keycloak.realm(keycloakProperties.realm()).users().list();
        for (var user : users) {
            if (!keycloakProperties.adminEmail().equals(user.getEmail())) {
                keycloak.realm(keycloakProperties.realm()).users().delete(user.getId());
            }
        }
    }

    public String getAdminAccessToken() {
        return individualApiTestService.login(new UserLoginRequest(keycloakProperties.adminEmail(), keycloakProperties.adminPassword()))
            .getAccessToken();
    }
}
