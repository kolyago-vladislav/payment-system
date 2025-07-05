package com.example.dto;

import lombok.Data;

@Data
public class KeycloakCredentialRepresentation {
    private String type;
    private String value;
    private Boolean temporary;
}
