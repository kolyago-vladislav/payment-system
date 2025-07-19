package com.example.spec.integration;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.AfterEach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.example.PersonServiceApplication;
import com.example.enivronment.config.testcontainer.TestcontainersApplicationContextInitializer;
import com.example.enivronment.data.DtoCreator;
import com.example.enivronment.service.DatabaseTestService;
import com.example.enivronment.service.PersonApiTestService;

@Slf4j
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    classes = PersonServiceApplication.class
)
@ContextConfiguration(initializers = TestcontainersApplicationContextInitializer.class)
@Setter(onMethod_ = {@Autowired})
public abstract class LifecycleSpecification {

    public static final String PERSON_EMAIL = "newuser@gmail.com";

    protected DtoCreator dtoCreator;
    protected PersonApiTestService personControllerService;
    protected DatabaseTestService databaseTestService;

    @AfterEach
    public void clear() {
        databaseTestService.truncateAllTables();
    }
}
