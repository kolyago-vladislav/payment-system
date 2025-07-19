package com.example.spec.integration.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatusCode;

import com.example.dto.IndividualPageRequest;
import com.example.person.dto.IndividualPageDto;
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
    void shouldReturnDtoWhenFindAllCalledWithEmailParam() {
        //when
        var response = personControllerService.findAll(new IndividualPageRequest(List.of(PERSON_EMAIL)));

        //then
        assertEquals(new IndividualPageDto().items(List.of(dtoCreator.buildIndividualDto())), response);
    }

    @Test
    void shouldReturnDtoWhenFindAllCalled() {
        //given
        var individualWriteDto = dtoCreator.buildIndividualWriteDto();
        individualWriteDto.setEmail("abc@gmail.com");
        personControllerService.register(individualWriteDto);

        //when
        var response = personControllerService.findAll(new IndividualPageRequest(List.of()));

        //then
        var individualDto = dtoCreator.buildIndividualDto();
        individualDto.setEmail(individualWriteDto.getEmail());
        var individualsDto = List.of(
            dtoCreator.buildIndividualDto(),
            individualDto
        );
        assertEquals(new IndividualPageDto().items(individualsDto), response);
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
