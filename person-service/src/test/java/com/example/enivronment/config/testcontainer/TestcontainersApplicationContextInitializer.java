package com.example.enivronment.config.testcontainer;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.enivronment.config.testcontainer.container.Containers;

public class TestcontainersApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Containers.run();
    }

}
