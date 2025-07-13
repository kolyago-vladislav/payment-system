package com.example.service;

import java.beans.Transient;
import java.util.UUID;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.exception.PersonException;
import com.example.mapper.IndividualMapper;
import com.example.person.dto.IndividualDto;
import com.example.person.dto.IndividualWriteDto;
import com.example.person.dto.IndividualWriteResponseDto;
import com.example.repository.IndividualRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndividualService {

    private final IndividualMapper mapper;
    private final IndividualRepository repository;

    @Transactional
    public IndividualWriteResponseDto register(
        IndividualWriteDto individualWriteDto
    ) {
        var individual = mapper.to(individualWriteDto);
        repository.save(individual);

        log.info("Individual[email={}] was successfully registered", individual.getUser().getEmail());

        return new IndividualWriteResponseDto(individual.getId().toString());
    }

    public IndividualDto findById(UUID id) {
        var individual = repository.findById(id)
            .orElseThrow(() -> new PersonException("Individual not found by id=[%s]", id));

        log.debug("Individual was successfully found by id=[{}]", id);

        return mapper.from(individual);
    }

    public IndividualDto findByEmail(String email) {
        var individual = repository.findByEmail(email)
            .orElseThrow(() -> new PersonException("Individual not found by email=[%s]", email));

        log.debug("Individual was successfully found by email=[{}]", email);

        return mapper.from(individual);
    }

    @Transactional
    public void softDelete(UUID id) {
        log.info("Individual[id={}] was successfully soft deleted", id);
        repository.softDelete(id);
    }

    @Transactional
    public void hardDelete(UUID id) {
        var individual = repository.findById(id)
            .orElseThrow(() -> new PersonException("Individual not found by uuid=[%s]", id));

        log.info("Individual[id={}] was successfully hard deleted", id);

        repository.delete(individual);
    }

    @Transient
    public IndividualWriteResponseDto update(
        UUID id,
        @Valid
        IndividualWriteDto dto
    ) {
        var individual = repository.findById(id)
            .orElseThrow(() -> new PersonException("Individual not found by id=[%s]", id));
        mapper.update(individual, dto);
        repository.save(individual);
        return new IndividualWriteResponseDto(individual.getId().toString());
    }
}
