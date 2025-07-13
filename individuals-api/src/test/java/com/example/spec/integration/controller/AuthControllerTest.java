package com.example.spec.integration.controller;

import org.junit.jupiter.api.Test;

import com.example.individual.dto.UserLoginRequest;
import com.example.individual.dto.IndividualWriteDto;
import com.example.spec.integration.LifecycleSpecification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuthControllerTest extends LifecycleSpecification {

	@Test
	void shouldCreateNewUserAndReturnAccessToken() {
		//when
		var request = buildUserRegistrationRequest();
		var response = apiService.register(request);
		var meResponse = apiService.getMe(response.getAccessToken());
		//then
		assertNotNull(response, "Response must not be null");
		assertNotNull(response.getAccessToken(), "Access token must not be null");
		assertEquals("Bearer", response.getTokenType(), "Token type must be Bearer");
		assertEquals(meResponse.getEmail(), request.getEmail());
	}

	@Test
	void shouldLoginAndReturnAccessToken() {
		//given
		var request = buildUserRegistrationRequest();
		apiService.register(request);

		//when
		var response = apiService.login(buildUserLoginRequest());
		var meResponse = apiService.getMe(response.getAccessToken());
		//then
		assertNotNull(response, "Response must not be null");
		assertNotNull(response.getAccessToken(), "Access token must not be null");
		assertEquals("Bearer", response.getTokenType(), "Token type must be Bearer");
		assertEquals(meResponse.getEmail(), request.getEmail());
	}

	@Test
	void shouldReturnUserInfo() {
		//given
		var registrationResponse = apiService.register(buildUserRegistrationRequest());

		//when
		var meResponse = apiService.getMe(registrationResponse.getAccessToken());

		assertNotNull(meResponse.getEmail(), buildUserRegistrationRequest().getEmail());
	}

	private IndividualWriteDto buildUserRegistrationRequest() {
		var request = new IndividualWriteDto();
		request.setEmail("newuser@gmail.com");
		request.setPassword("password");
		request.setConfirmPassword("password");
		return request;
	}

	private UserLoginRequest buildUserLoginRequest() {
		var request = new UserLoginRequest();
		request.setEmail("newuser@gmail.com");
		request.setPassword("password");
		return request;
	}
}
