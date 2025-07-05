package com.example.dto;

import lombok.Data;

@Data
public class KeycloakCredentialDto {
    private String type;
    private String value;
    private Boolean temporary;
}
