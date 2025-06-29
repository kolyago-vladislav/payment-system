package com.example.controller;

import com.example.api.AuthApi;
import com.example.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController implements AuthApi {


    @Override
    public ResponseEntity<UserInfoResponse> getMe() {
        return null;
    }

    @Override
    public ResponseEntity<TokenResponse> login(UserLoginRequest userLoginRequest) {
        return null;
    }

    @Override
    public ResponseEntity<TokenResponse> refreshToken(TokenRefreshRequest tokenRefreshRequest) {
        return null;
    }

    @Override
    public ResponseEntity<TokenResponse> registration(UserRegistrationRequest userRegistrationRequest) {
        return null;
    }
}
