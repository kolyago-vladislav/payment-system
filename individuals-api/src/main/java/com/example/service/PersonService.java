package com.example.service;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import com.example.individual.dto.IndividualDto;
import com.example.individual.dto.IndividualPageDto;
import com.example.individual.dto.IndividualWriteDto;
import com.example.individual.dto.IndividualWriteResponseDto;
import com.example.mapper.PersonMapper;
import com.example.person.api.PersonApiClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonApiClient personApiClient;
    private final PersonMapper personMapper;

    @WithSpan(value = "personService.findById")
    public Mono<IndividualDto> findById(UUID id) {
        return Mono.fromCallable(() -> personApiClient.findById(id))
            .map(dto -> personMapper.from(dto.getBody()))
            .subscribeOn(Schedulers.boundedElastic());
    }

    @WithSpan(value = "personService.findByEmail")
    public Mono<IndividualPageDto> findAll(List<String> email) {
        return Mono.fromCallable(() -> personApiClient.findAll(email))
            .map(dto -> personMapper.from(dto.getBody()))
            .subscribeOn(Schedulers.boundedElastic());
    }

    @WithSpan("personService.register")
    public Mono<IndividualWriteResponseDto> register(IndividualWriteDto request) {
        return Mono
            .fromCallable(() -> personApiClient.registration(personMapper.from(request)))
            .mapNotNull(HttpEntity::getBody)
            .map(personMapper::from)
            .subscribeOn(Schedulers.boundedElastic())
            .doOnNext(t -> log.info("Person registered id={}", t.getId()));
    }

    @WithSpan(value = "personService.update")
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

    @WithSpan(value = "personService.hardDelete")
    public Mono<Void> hardDelete(String id) {
        return Mono.fromRunnable(() -> personApiClient.hardDelete(UUID.fromString(id)))
            .subscribeOn(Schedulers.boundedElastic()).then();
    }

    @WithSpan(value = "personService.delete")
    public Mono<Void> delete(UUID id) {
        return Mono.fromRunnable(() -> personApiClient.delete(id))
            .subscribeOn(Schedulers.boundedElastic()).then();
    }
}
