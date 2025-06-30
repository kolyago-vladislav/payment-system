package com.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.config.property.KeycloakProperties;
import com.example.dto.TokenResponse;
import com.example.mapper.TokenResponseMapper;

import static org.keycloak.OAuth2Constants.CLIENT_ID;
import static org.keycloak.OAuth2Constants.CLIENT_SECRET;
import static org.keycloak.OAuth2Constants.GRANT_TYPE;
import static org.keycloak.OAuth2Constants.REFRESH_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final KeycloakProperties keycloakProperties;
    private final RestTemplate restTemplate;
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

    public TokenResponse refreshToken(String refreshToken) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var map = new LinkedMultiValueMap<String, String>();
        map.add(GRANT_TYPE, REFRESH_TOKEN);
        map.add(REFRESH_TOKEN, refreshToken);
        map.add(CLIENT_ID, keycloakProperties.clientId());
        map.add(CLIENT_SECRET, keycloakProperties.clientSecret());

        var entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        var response = restTemplate.exchange(keycloakProperties.tokenUrl(), HttpMethod.POST, entity, AccessTokenResponse.class);
        return tokenResponseMapper.to(response.getBody());
    }
}
