package com.example.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.AuthApi;
import com.example.dto.TokenRefreshRequest;
import com.example.dto.TokenResponse;
import com.example.dto.UserInfoResponse;
import com.example.dto.UserLoginRequest;
import com.example.dto.UserRegistrationRequest;
import com.example.service.TokenService;
import com.example.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class AuthController implements AuthApi {

    private final UserService userService;
    private final TokenService tokenService;

    @Override
    public ResponseEntity<UserInfoResponse> getMe() {
        return ResponseEntity.ok(userService.getCurrentUserInfo());
    }

    @Override
    public ResponseEntity<TokenResponse> login(UserLoginRequest userLoginRequest) {
        return ResponseEntity.ok(userService.login(userLoginRequest));
    }

    @Override
    public ResponseEntity<TokenResponse> refreshToken(TokenRefreshRequest tokenRefreshRequest) {
        return ResponseEntity.ok(tokenService.refreshToken(tokenRefreshRequest.getRefreshToken()));
    }

    @Override
    public ResponseEntity<TokenResponse> registration(UserRegistrationRequest userRegistrationRequest) {
        return ResponseEntity.ok(userService.register(userRegistrationRequest));
    }
}
