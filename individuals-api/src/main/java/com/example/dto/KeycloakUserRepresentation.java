package com.example.dto;

import lombok.Data;

@Data
public class KeycloakUserRepresentation {
    private String id;
    private String username;
    private Boolean enabled;
    private Boolean emailVerified;
    private String email;
}
