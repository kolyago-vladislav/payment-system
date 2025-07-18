package com.example.spec.integration.controller;

import org.junit.jupiter.api.Test;

import com.example.spec.integration.LifecycleSpecification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class AuthControllerTest extends LifecycleSpecification {

	@Test
	void shouldCreateNewUserAndReturnAccessToken() {
		//when
		var request = dtoCreator.buildIndividualWriteDto();

		var response = individualControllerService.register(request);
		var meResponse = individualControllerService.getMe(response.getAccessToken());

		var personId = keycloakApiTestService.getUserAttributes(request.getEmail()).get("personId").get(0);

		//then
		assertEquals(PERSON_ID, personId);
		assertNotNull(response, "Response must not be null");
		assertNotNull(response.getAccessToken(), "Access token must not be null");
		assertEquals("Bearer", response.getTokenType(), "Token type must be Bearer");
		assertEquals(meResponse.getEmail(), request.getEmail());
	}

	@Test
	void shouldLoginAndReturnAccessToken() {
		//given
		var request = dtoCreator.buildIndividualWriteDto();

		individualControllerService.register(request);

		//when
		var response = individualControllerService.login(dtoCreator.buildUserLoginRequest());
		var meResponse = individualControllerService.getMe(response.getAccessToken());

		//then
		assertNotNull(response, "Response must not be null");
		assertNotNull(response.getAccessToken(), "Access token must not be null");
		assertEquals("Bearer", response.getTokenType(), "Token type must be Bearer");
		assertEquals(meResponse.getEmail(), request.getEmail());
	}

	@Test
	void shouldReturnUserInfo() {
		//given
		var individualWriteDto = dtoCreator.buildIndividualWriteDto();

		var registrationResponse = individualControllerService.register(individualWriteDto);

		//when
		var meResponse = individualControllerService.getMe(registrationResponse.getAccessToken());

		assertNotNull(meResponse.getEmail(), dtoCreator.buildIndividualWriteDto().getEmail());
	}
}
