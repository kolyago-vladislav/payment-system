package com.example.service;

import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.example.client.KeycloakClient;
import com.example.dto.TokenRefreshRequest;
import com.example.dto.TokenResponse;
import com.example.dto.UserLoginRequest;
import com.example.mapper.TokenResponseMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenResponseMapper tokenResponseMapper;
    private final KeycloakClient keycloakClient;

    public Mono<TokenResponse> login(UserLoginRequest userLoginRequest) {
        return keycloakClient.login(userLoginRequest)
            .doOnNext(t -> log.info("Token was successfully generated for email={}", userLoginRequest.getEmail()))
            .doOnError(e -> log.warn("Failed to generate token for email={}", userLoginRequest.getEmail(), e))
            .map(tokenResponseMapper::toTokenResponse);
    }

    public Mono<TokenResponse> refreshToken(TokenRefreshRequest tokenRefreshRequest) {
        return keycloakClient.refreshToken(tokenRefreshRequest)
            .doOnNext(r -> log.info("Token was successfully refreshed"))
            .map(tokenResponseMapper::toTokenResponse);
    }
}
