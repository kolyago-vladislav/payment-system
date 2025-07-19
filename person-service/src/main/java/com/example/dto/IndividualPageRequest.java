package com.example.dto;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

public record IndividualPageRequest(List<String> emails) {

    public IndividualPageRequest {
        emails = isEmpty(emails) ? null : emails;
    }
}
