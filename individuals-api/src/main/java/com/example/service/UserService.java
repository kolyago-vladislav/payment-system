package com.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;

import org.springframework.stereotype.Service;

import com.example.dto.TokenResponse;
import com.example.dto.UserRegistrationRequest;
import com.example.exception.IndividualException;
import com.example.mapper.CredentialRepresentationMapper;
import com.example.mapper.UserRepresentationMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    public static final String REGEX_GET_SUBSTRING_AFTER_LAST_SLASH = ".*/([^/]+)$";

    private final RealmResource realmResource;
    private final UserRepresentationMapper userRepresentationMapper;
    private final CredentialRepresentationMapper credentialRepresentationMapper;
    private final TokenService tokenService;

    public TokenResponse register(UserRegistrationRequest request) {
        var user = userRepresentationMapper.to(request);

        try (var response = realmResource.users().create(user)) {
            if (response.getStatus() != 201) {
                log.error("User[email={}] is not created. Something went wrong", user.getEmail());
                throw new IndividualException("User[email=%s] is not created. Something went wrong", user.getEmail());
            }

            var userId = extractIdFromPath(response.getLocation().getPath());

            setUserPassword(user, userId, request);

            log.info("User[email={}] was successfully created with id={}", user.getEmail(), userId);

            return tokenService.generateAccessToken(request.getEmail(), request.getPassword());
        }
    }

    private void setUserPassword(
        UserRepresentation user,
        String userId,
        UserRegistrationRequest request
    ) {
        try {
            var credentialRepresentation = credentialRepresentationMapper.to(request);
            var userResource = realmResource.users().get(userId);
            userResource.resetPassword(credentialRepresentation);
        } catch (Exception e) {
            realmResource.users().get(userId).remove();
            log.error("User[email={}] was created but password wasn't set correctly", user.getEmail());
            throw new IndividualException("User[email=%s] was created but password wasn't set correctly", user.getEmail());
        }
    }

    private String extractIdFromPath(String path) {
        return path.replaceAll(REGEX_GET_SUBSTRING_AFTER_LAST_SLASH, "$1");
    }
}
