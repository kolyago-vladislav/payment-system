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
import com.example.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class AuthController implements AuthApi {
    private final UserService userService;

    @Override
    public ResponseEntity<UserInfoResponse> getMe() {
        return ResponseEntity.ok(userService.getCurrentUserInfo());
    }

    @Override
    public ResponseEntity<TokenResponse> login(UserLoginRequest userLoginRequest) {
        return ResponseEntity.ok(new TokenResponse()
                .accessToken("login-uladzislau.kaliaha"));
    }

    @Override
    public ResponseEntity<TokenResponse> refreshToken(TokenRefreshRequest tokenRefreshRequest) {
        return ResponseEntity.ok(new TokenResponse()
                .accessToken("refreshToken-uladzislau.kaliaha"));
    }

    @Override
    public ResponseEntity<TokenResponse> registration(UserRegistrationRequest userRegistrationRequest) {
        return ResponseEntity.ok(userService.register(userRegistrationRequest));
    }
}
