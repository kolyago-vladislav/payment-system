package com.example.dto;

public record KeycloakCredentialRepresentation(
    String type,
    String value,
    Boolean temporary) {
}
