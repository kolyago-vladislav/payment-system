package com.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import org.springframework.stereotype.Service;

import com.example.config.property.KeycloakProperties;
import com.example.dto.TokenResponse;
import com.example.mapper.TokenResponseMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final KeycloakProperties keycloakProperties;

    private final TokenResponseMapper tokenResponseMapper;

    public TokenResponse generateAccessToken(String email, String password) {
        try (var keycloak = buildKeycloakClient(email, password)) {
            var accessToken = keycloak.tokenManager().getAccessToken();

            log.info("Token was successfully generated for email={}", email);

            return tokenResponseMapper.to(accessToken);
        }
    }

    private Keycloak buildKeycloakClient(
        String email,
        String password
    ) {
        return KeycloakBuilder.builder()
            .serverUrl(keycloakProperties.serverUrl())
            .realm(keycloakProperties.realm())
            .username(email)
            .password(password)
            .clientId(keycloakProperties.clientId())
            .clientSecret(keycloakProperties.clientSecret())
            .grantType(OAuth2Constants.PASSWORD)
            .build();
    }
}
