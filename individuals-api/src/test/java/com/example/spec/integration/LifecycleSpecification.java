package com.example.spec.integration;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.AfterEach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.example.IndividualApiApplication;
import com.example.enivronment.config.testcontainer.TestcontainersApplicationContextInitializer;
import com.example.enivronment.config.testcontainer.data.DtoCreator;
import com.example.enivronment.config.testcontainer.service.IndividualApiTestService;
import com.example.enivronment.config.testcontainer.service.KeycloakApiTestService;
import com.example.enivronment.config.testcontainer.service.PersonApiTestService;
import com.example.mapper.PersonMapper;

@Slf4j
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    classes = IndividualApiApplication.class
)
@ContextConfiguration(initializers = TestcontainersApplicationContextInitializer.class)
@Setter(onMethod_ = {@Autowired})
public abstract class LifecycleSpecification {

    public static final String PERSON_ID = "3ddcecd5-5004-4a8a-9447-ed110912b7ee";
    public static final String PERSON_EMAIL = "newuser@gmail.com";

    protected DtoCreator dtoCreator;
    protected PersonMapper personMapper;
    protected IndividualApiTestService individualControllerService;
    protected PersonApiTestService personControllerService;
    protected KeycloakApiTestService keycloakApiTestService;

    @AfterEach
    public void clear() {
        keycloakApiTestService.clear();
    }
}
