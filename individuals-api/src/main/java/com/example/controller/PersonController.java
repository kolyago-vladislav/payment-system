package com.example.controller;

import reactor.core.publisher.Mono;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.Email;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.example.individual.api.PersonApi;
import com.example.individual.dto.IndividualDto;
import com.example.individual.dto.IndividualPageDto;
import com.example.individual.dto.IndividualWriteDto;
import com.example.individual.dto.IndividualWriteResponseDto;
import com.example.service.PersonService;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class PersonController implements PersonApi {

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
    public Mono<ResponseEntity<IndividualPageDto>> findAll(
        List<@Email String> emails,
        ServerWebExchange exchange
    ) {
        return personService
            .findAll(emails)
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
