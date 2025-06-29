package com.example.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("application.keycloak")
public record KeycloakProperties(
    String serverUrl,
    String clientSecret,
    String clientId,
    String realm,
    String adminUsername,
    String adminPassword,
    String adminClientId
) {

}
