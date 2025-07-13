package com.example.service;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.example.individual.dto.IndividualDto;
import com.example.individual.dto.IndividualWriteDto;
import com.example.individual.dto.IndividualWriteResponseDto;
import com.example.mapper.PersonMapper;
import com.example.person.api.PersonApiClient;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonApiClient personApiClient;
    private final PersonMapper personMapper;

    public Mono<IndividualDto> findById(UUID id) {
        return Mono.fromCallable(() -> personApiClient.findById(id))
            .map(dto -> personMapper.from(dto.getBody()))
            .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<IndividualDto> findByEmail(String email) {
        return Mono.fromCallable(() -> personApiClient.findByEmail(email))
            .map(dto -> personMapper.from(dto.getBody()))
            .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<IndividualWriteResponseDto> register(IndividualWriteDto request) {
        return Mono
            .fromCallable(() -> personApiClient.registration(personMapper.from(request)).getBody())
            .map(personMapper::from)
            .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<IndividualWriteResponseDto> update(
        UUID id,
        Mono<IndividualWriteDto> request
    ) {
        return request
            .flatMap(dto ->
                Mono.fromCallable(() -> personApiClient.update(id, personMapper.from(dto)))
                    .subscribeOn(Schedulers.boundedElastic())
                    .flatMap(responseEntity ->
                        Mono.justOrEmpty(personMapper.from(responseEntity.getBody()))));
    }

    public Mono<Void> hardDelete(String id) {
        return Mono.fromRunnable(() -> personApiClient.hardDelete(UUID.fromString(id)))
            .subscribeOn(Schedulers.boundedElastic()).then();
    }

    public Mono<Void> delete(UUID id) {
        return Mono.fromRunnable(() -> personApiClient.delete(id))
            .subscribeOn(Schedulers.boundedElastic()).then();
    }
}
