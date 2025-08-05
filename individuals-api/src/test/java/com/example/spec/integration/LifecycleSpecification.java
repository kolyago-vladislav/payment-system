package com.example.spec.integration;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import javax.annotation.PostConstruct;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.AfterEach;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.example.IndividualApiApplication;
import com.example.enivronment.config.testcontainer.TestcontainersApplicationContextInitializer;
import com.example.enivronment.config.testcontainer.data.DtoCreator;
import com.example.enivronment.config.testcontainer.service.IndividualApiTestService;
import com.example.enivronment.config.testcontainer.service.KeycloakApiTestService;
import com.example.enivronment.config.testcontainer.service.PersonApiTestService;
import com.example.enivronment.config.testcontainer.service.TransactionApiTestService;
import com.example.enivronment.config.testcontainer.service.WalletApiTestService;
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
    public static final String WALLET_ID = "19ed22c9-6923-4f4e-867f-b86e5254d80a";
    public static final String TRANSACTION_ID = "71e7a0dc-9078-4d45-96b2-f168baa9901a";
    public static final String PERSON_EMAIL = "newuser@gmail.com";


    protected DtoCreator dtoCreator;
    protected PersonMapper personMapper;
    protected IndividualApiTestService individualControllerService;
    protected PersonApiTestService personControllerService;
    protected KeycloakApiTestService keycloakApiTestService;
    protected WalletApiTestService walletApiTestService;
    protected TransactionApiTestService transactionApiTestService;

    @MockitoSpyBean
    protected Clock clock;

    public static final LocalDateTime DEFAULT_LOCAL_DATE_TIME = LocalDateTime.of(2024, 10, 2, 7, 4, 3);

    @PostConstruct
    void init() {
        Mockito
            .doReturn(DEFAULT_LOCAL_DATE_TIME.toInstant(ZoneOffset.UTC))
            .when(clock).instant();
    }

    @AfterEach
    public void clear() {
        keycloakApiTestService.clear();
    }
}
