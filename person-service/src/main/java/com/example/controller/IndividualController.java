package com.example.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.IndividualPageRequest;
import com.example.person.api.PersonApi;
import com.example.person.dto.IndividualDto;
import com.example.person.dto.IndividualPageDto;
import com.example.person.dto.IndividualWriteDto;
import com.example.person.dto.IndividualWriteResponseDto;
import com.example.service.IndividualService;

@RestController
@RequiredArgsConstructor
public class IndividualController implements PersonApi {

    private final IndividualService individualService;

    @Override
    public ResponseEntity<IndividualWriteResponseDto> registration(
        @Valid
        IndividualWriteDto individualWriteDto
    ) {
        return ResponseEntity.ok(individualService.register(individualWriteDto));
    }

    @Override
    public ResponseEntity<IndividualDto> findById(UUID id) {
        return ResponseEntity.ok(individualService.findById(id));
    }

    @Override
    public ResponseEntity<IndividualPageDto> findAll(
        List<String> emails
    ) {
        var request = new IndividualPageRequest(
            emails
        );
        return ResponseEntity.ok(individualService.findAll(request));
    }

    @Override
    public ResponseEntity<IndividualWriteResponseDto> update(
        UUID uuid,
        @Valid IndividualWriteDto individualWriteDto
    ) {
        return ResponseEntity.ok(individualService.update(uuid, individualWriteDto));
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        individualService.softDelete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> compensateRegistration(UUID id) {
        individualService.hardDelete(id);
        return ResponseEntity.ok().build();
    }
}
