package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KeycloakAccessTokenResponse(
    @JsonProperty("access_token")
    String token,

    @JsonProperty("refresh_token")
    String refreshToken,

    @JsonProperty("expires_in")
    long expiresIn,

    @JsonProperty("token_type")
    String tokenType
) {

}
