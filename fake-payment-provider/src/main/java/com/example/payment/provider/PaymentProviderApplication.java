package com.example.payment.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class PaymentProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentProviderApplication.class, args);
    }

}
