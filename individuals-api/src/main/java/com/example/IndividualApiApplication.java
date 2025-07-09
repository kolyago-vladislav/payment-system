package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class IndividualApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(IndividualApiApplication.class, args);
	}

}
