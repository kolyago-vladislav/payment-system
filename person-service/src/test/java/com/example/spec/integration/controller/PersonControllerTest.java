package com.example.spec.integration.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatusCode;

import com.example.spec.integration.LifecycleSpecification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PersonControllerTest extends LifecycleSpecification {

    private String id;

    @BeforeEach
    void setUp() {
        var individualWriteDto = dtoCreator.buildIndividualWriteDto();

        var response = personControllerService.register(individualWriteDto);

        id = response;

        assertNotNull(response);
    }

    @Test
    void shouldReturnDtoWhenFindByIdCalled() {
        //when
        var response = personControllerService.findById(id);

        //then
        assertEquals(dtoCreator.buildIndividualDto(), response);
    }

    @Test
    void shouldReturnDtoWhenFindByEmailCalled() {
        //when
        var response = personControllerService.findByEmail(PERSON_EMAIL);

        //then
        assertEquals(dtoCreator.buildIndividualDto(), response);
    }

    @Test
    void shouldUpdateEntity() {
        var individualWriteDto = dtoCreator.buildForUpdate();

        var response = personControllerService.update(id, individualWriteDto);
        assertEquals(id, response);
    }

    @Test
    void shouldDeleteEntity() {
        //when
        var response = personControllerService.delete(id);

        //then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

}
