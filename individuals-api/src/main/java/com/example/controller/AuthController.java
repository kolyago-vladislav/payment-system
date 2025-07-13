package com.example.controller;

import reactor.core.publisher.Mono;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.example.individual.api.AuthApi;
import com.example.individual.dto.TokenRefreshRequest;
import com.example.individual.dto.TokenResponse;
import com.example.individual.dto.UserInfoResponse;
import com.example.individual.dto.UserLoginRequest;
import com.example.individual.dto.UserRegistrationRequest;
import com.example.service.TokenService;
import com.example.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class AuthController implements AuthApi {

    private final UserService userService;
    private final TokenService tokenService;

    @Override
    public Mono<ResponseEntity<UserInfoResponse>> getMe(ServerWebExchange exchange) {
        return userService.getCurrentUserInfo().map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<TokenResponse>> login(
        Mono<UserLoginRequest> userLoginRequest,
        ServerWebExchange exchange
    ) {
        return userLoginRequest
            .flatMap(tokenService::login)
            .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<TokenResponse>> refreshToken(
        Mono<TokenRefreshRequest> tokenRefreshRequest,
        ServerWebExchange exchange
    ) {
        return tokenRefreshRequest
            .flatMap(tokenService::refreshToken)
            .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<TokenResponse>> registration(
        Mono<UserRegistrationRequest> userRegistrationRequest,
        ServerWebExchange exchange
    ) {
        return userRegistrationRequest
            .flatMap(userService::register)
            .map(ResponseEntity::ok);
    }
}
