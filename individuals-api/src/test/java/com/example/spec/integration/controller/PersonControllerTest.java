package com.example.spec.integration.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatusCode;

import com.example.spec.integration.LifecycleSpecification;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonControllerTest extends LifecycleSpecification {

    @BeforeEach
    void setUp() {
        var individualWriteDto = dtoCreator.buildIndividualWriteDto();
        individualControllerService.register(individualWriteDto);
    }

    @Test
    void shouldReturnDtoWhenFindByIdCalled() {
        //when
        var response = personControllerService.findById(PERSON_ID);

        //then
        assertEquals(dtoCreator.buildIndividualDto(), response);
    }

    @Test
    void shouldReturnDtoWhenFindByEmailCalled() {
        //when
        var response = personControllerService.findAll(PERSON_EMAIL);

        //then
        assertEquals(dtoCreator.buildIndividualPageDto(), response);
    }

    @Test
    void shouldUpdateEntity() {
        //when
        var response = personControllerService.update(PERSON_ID, personMapper.from(dtoCreator.buildForUpdate()));

        //then
        assertEquals(PERSON_ID, response);
    }

    @Test
    void shouldDeleteEntity() {
        //when
        var response = personControllerService.delete(PERSON_ID);

        //then
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

}
