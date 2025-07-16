package com.example.spec.integration.controller;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.example.person.dto.IndividualWriteResponseDto;
import com.example.spec.integration.LifecycleSpecification;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonControllerTest extends LifecycleSpecification {

    @BeforeEach
    void setUp() {
        var individualWriteDto = dtoCreator.buildIndividualWriteDto();
        Mockito
            .when(personApiClient.registration(personMapper.from(individualWriteDto)))
            .thenReturn(ResponseEntity.ofNullable(new IndividualWriteResponseDto(PERSON_ID)));
        individualControllerService.register(individualWriteDto);
    }

    @Test
    void shouldReturnDtoWhenFindByIdCalled() {
        //when
        Mockito
            .when(personApiClient.findById(UUID.fromString(PERSON_ID)))
            .thenReturn(ResponseEntity.ofNullable(personMapper.from(dtoCreator.buildIndividualDto())));

        var response = personControllerService.findById(PERSON_ID);

        //then
        assertEquals(dtoCreator.buildIndividualDto(), response);
    }

    @Test
    void shouldReturnDtoWhenFindByEmailCalled() {
        //when
        Mockito
            .when(personApiClient.findAll(List.of(PERSON_EMAIL)))
            .thenReturn(ResponseEntity.ofNullable(personMapper.from(dtoCreator.buildIndividualPageDto())));

        var response = personControllerService.findAll(PERSON_EMAIL);

        //then
        assertEquals(dtoCreator.buildIndividualPageDto(), response);
    }

    @Test
    void shouldUpdateEntity() {
        //when
        Mockito
            .when(personApiClient.update(UUID.fromString(PERSON_ID), personMapper.from(dtoCreator.buildForUpdate())))
            .thenReturn(ResponseEntity.ofNullable(new IndividualWriteResponseDto(PERSON_ID)));

        var response = personControllerService.update(PERSON_ID, personMapper.from(dtoCreator.buildForUpdate()));

        //then
        assertEquals(PERSON_ID, response);
    }

    @Test
    void shouldDeleteEntity() {
        //when
        Mockito
            .when(personApiClient.delete(UUID.fromString(PERSON_ID)))
            .thenReturn(ResponseEntity.ok().build());

        var response = personControllerService.delete(PERSON_ID);

        //then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

}
