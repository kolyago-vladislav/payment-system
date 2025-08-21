package com.example.payment.provider.enivronment.config.testcontainer;

import com.example.payment.provider.enivronment.config.testcontainer.container.Containers;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class TestcontainersApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Containers.run();
    }

}
