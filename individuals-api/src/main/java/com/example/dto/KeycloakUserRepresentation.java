package com.example.dto;

import java.util.Map;

public record KeycloakUserRepresentation(
    String id,
    String username,
    Boolean enabled,
    Boolean emailVerified,
    String email,
    Map<String, String> attributes
) {

}
