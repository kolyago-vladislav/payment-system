package com.example.service;

import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.client.KeycloakClient;
import com.example.config.property.KeycloakProperties;
import com.example.dto.KeycloakAccessTokenResponse;
import com.example.dto.TokenRefreshRequest;
import com.example.dto.TokenResponse;
import com.example.dto.UserLoginRequest;
import com.example.mapper.TokenResponseMapper;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.CLIENT_ID;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.CLIENT_SECRET;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.GRANT_TYPE;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.PASSWORD;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.USERNAME;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final WebClient webClient;
    private final KeycloakProperties keycloakProperties;
    private final TokenResponseMapper tokenResponseMapper;
    private final KeycloakClient keycloakClient;

    public Mono<TokenResponse> login(UserLoginRequest userLoginRequest) {
        var formData = new LinkedMultiValueMap<String, String>();
        formData.add(GRANT_TYPE, PASSWORD);
        formData.add(USERNAME, userLoginRequest.getEmail());
        formData.add(PASSWORD, userLoginRequest.getPassword());
        formData.add(CLIENT_ID, keycloakProperties.clientId());
        formData.add(CLIENT_SECRET, keycloakProperties.clientSecret());

        return webClient.post()
            .uri(keycloakProperties.tokenUrl()) // обычно "/realms/{realm}/protocol/openid-connect/token"
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue(formData)
            .retrieve()
            .bodyToMono(KeycloakAccessTokenResponse.class)
            .map(tokenResponseMapper::to)
            .doOnNext(token -> log.info("Token was successfully generated for email={}", userLoginRequest.getEmail()));
    }

    public Mono<TokenResponse> refreshToken(TokenRefreshRequest tokenRefreshRequest) {
        var formData = new LinkedMultiValueMap<String, String>();
        formData.add(GRANT_TYPE, REFRESH_TOKEN);
        formData.add(REFRESH_TOKEN, tokenRefreshRequest.getRefreshToken());
        formData.add(CLIENT_ID, keycloakProperties.clientId());
        formData.add(CLIENT_SECRET, keycloakProperties.clientSecret());

        return webClient.post()
            .uri(keycloakProperties.tokenUrl())
            .bodyValue(formData)
            .retrieve()
            .bodyToMono(KeycloakAccessTokenResponse.class)
            .doOnNext(response -> log.info("Token was successfully refreshed"))
            .map(tokenResponseMapper::to);
    }
}
