package com.example.transaction.spec.integration;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;

import com.example.transaction.TransactionServiceApplication;
import com.example.transaction.environment.config.testcontainer.TestcontainersApplicationContextInitializer;

@Getter
@Slf4j
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = TransactionServiceApplication.class
)
@ContextConfiguration(initializers = TestcontainersApplicationContextInitializer.class)
public abstract class LifecycleSpecification {

    @LocalServerPort
    private int port;

    public static final String USER_ID = "123e4567-e89b-12d3-a456-426614174010";

}
