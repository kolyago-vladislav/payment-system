package com.example.service;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.example.client.KeycloakClient;
import com.example.config.property.KeycloakProperties;
import com.example.individual.dto.TokenRefreshRequest;
import com.example.individual.dto.TokenResponse;
import com.example.individual.dto.UserLoginRequest;
import com.example.mapper.TokenResponseMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final KeycloakProperties keycloakProperties;
    private final TokenResponseMapper tokenResponseMapper;
    private final KeycloakClient keycloakClient;

    @WithSpan("tokenService.login")
    public Mono<TokenResponse> login(UserLoginRequest userLoginRequest) {
        return keycloakClient.login(userLoginRequest)
            .doOnNext(t -> log.info("Token was successfully generated for email={}", userLoginRequest.getEmail()))
            .doOnError(e -> log.error("Failed to generate token for email={}", userLoginRequest.getEmail(), e))
            .map(tokenResponseMapper::toTokenResponse);
    }

    @WithSpan(value = "tokenService.refreshToken")
    public Mono<TokenResponse> refreshToken(TokenRefreshRequest tokenRefreshRequest) {
        return keycloakClient.refreshToken(tokenRefreshRequest)
            .doOnNext(r -> log.info("Token was successfully refreshed"))
            .map(tokenResponseMapper::toTokenResponse);
    }

    public Mono<TokenResponse> obtainAdminServiceToken() {
        var adminLoginRequest = new UserLoginRequest(
            keycloakProperties.adminEmail(),
            keycloakProperties.adminPassword()
        );
        return login(adminLoginRequest);
    }
}
