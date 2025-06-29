package com.example.config;

import lombok.RequiredArgsConstructor;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.config.property.KeycloakProperties;

@Configuration
@RequiredArgsConstructor
public class KeycloakConfig {

    private final KeycloakProperties keycloakProperties;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.serverUrl())
                .realm(keycloakProperties.realm())
                .username(keycloakProperties.adminUsername())
                .password(keycloakProperties.adminPassword())
                .clientId(keycloakProperties.adminClientId())
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }

    @Bean
    public RealmResource realmResource(Keycloak keycloak) {
        return keycloak.realm(keycloakProperties.realm());
    }
}
