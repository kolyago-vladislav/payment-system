package com.example.spec.integration.controller;

import org.junit.jupiter.api.Test;

import com.example.dto.UserLoginRequest;
import com.example.dto.UserRegistrationRequest;
import com.example.spec.integration.LifecycleSpecification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuthControllerTest extends LifecycleSpecification {

	@Test
	void shouldCreateNewUserAndReturnAccessToken() {
		//when
		var response = apiService.register(buildUserRegistrationRequest());

		//then
		assertNotNull(response, "Response must not be null");
		assertNotNull(response.getAccessToken(), "Access token must not be null");
		assertEquals("Bearer", response.getTokenType(), "Token type must be Bearer");
	}

	@Test
	void shouldLoginAndReturnAccessToken() {
		//given
		var registrationResponse = apiService.register(buildUserRegistrationRequest());

		//when
		var loginResponse = apiService.login(buildUserLoginRequest());

		//then
		assertNotNull(loginResponse, "Response must not be null");
		assertNotNull(loginResponse.getAccessToken(), "Access token must not be null");
		assertEquals("Bearer", loginResponse.getTokenType(), "Token type must be Bearer");
	}

	@Test
	void shouldReturnUserInfo() {
		//given
		var registrationResponse = apiService.register(buildUserRegistrationRequest());

		//when
		var meResponse = apiService.getMe(registrationResponse.getAccessToken());

		assertNotNull(meResponse.getEmail(), buildUserRegistrationRequest().getEmail());
	}

	private UserRegistrationRequest buildUserRegistrationRequest() {
		var request = new UserRegistrationRequest();
		request.setEmail("newUser@gmail.com");
		request.setPassword("password");
		request.setConfirmPassword("password");
		return request;
	}

	private UserLoginRequest buildUserLoginRequest() {
		var request = new UserLoginRequest();
		request.setEmail("newUser@gmail.com");
		request.setPassword("password");
		return request;
	}
}
