package com.example.spec.integration.controller;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.example.person.api.PersonApiClient;
import com.example.person.dto.IndividualWriteResponseDto;
import com.example.spec.integration.LifecycleSpecification;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonControllerTest extends LifecycleSpecification {

    @MockitoBean
    private PersonApiClient personApiClient;

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
            .when(personApiClient.findByEmail(PERSON_EMAIL))
            .thenReturn(ResponseEntity.ofNullable(personMapper.from(dtoCreator.buildIndividualDto())));

        var response = personControllerService.findByEmail(PERSON_EMAIL);

        //then
        assertEquals(dtoCreator.buildIndividualDto(), response);
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
