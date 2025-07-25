package com.example.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("application.keycloak")
public record KeycloakProperties(
    String serverUrl,
    String realmUrl,
    String tokenUrl,
    String clientSecret,
    String clientId,
    String realm,
    String adminEmail,
    String adminPassword,
    String adminClientId
) {

}
