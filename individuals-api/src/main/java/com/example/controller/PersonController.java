package com.example.controller;

import reactor.core.publisher.Mono;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.example.individual.api.PersonsApi;
import com.example.individual.dto.IndividualDto;
import com.example.individual.dto.IndividualWriteDto;
import com.example.individual.dto.IndividualWriteResponseDto;
import com.example.service.PersonService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class PersonController implements PersonsApi {

    private final PersonService personService;

    @Override
    public Mono<ResponseEntity<IndividualDto>> findById(
        UUID id,
        ServerWebExchange exchange
    ) {
        return personService
            .findById(id)
            .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<IndividualDto>> findByEmail(
        String email,
        ServerWebExchange exchange
    ) {
        return personService
            .findByEmail(email)
            .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<IndividualWriteResponseDto>> update(
        UUID id,
        Mono<IndividualWriteDto> individualWriteDto,
        ServerWebExchange exchange
    ) {
        return personService
            .update(id, individualWriteDto)
            .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Void>> delete(
        UUID id,
        ServerWebExchange exchange
    ) {
        return personService
            .delete(id)
            .then(Mono.fromSupplier(() -> ResponseEntity.ok().build()));
    }
}
