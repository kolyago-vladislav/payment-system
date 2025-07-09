package com.example.dto;

public record KeycloakUserRepresentation(
    String id,
    String username,
    Boolean enabled,
    Boolean emailVerified,
    String email
) {

}
