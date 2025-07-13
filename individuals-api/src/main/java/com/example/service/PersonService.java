package com.example.service;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.example.individual.dto.IndividualWriteDto;
import com.example.mapper.PersonMapper;
import com.example.person.api.PersonsApiClient;
import com.example.person.dto.IndividualWriteResponseDto;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonsApiClient personsApiClient;
    private final PersonMapper personMapper;

    public Mono<IndividualWriteResponseDto> register(IndividualWriteDto request) {
        var dto = personMapper.from(request);

        return Mono.fromCallable(() -> personsApiClient.registration(dto).getBody())
            .subscribeOn(Schedulers.boundedElastic());
    }
}
