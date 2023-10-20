package com.phoenix.expensetrackerservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phoenix.expensetrackerservice.client.AuthServiceClient;
import com.phoenix.expensetrackerservice.config.AuthServiceConfig;
import com.phoenix.expensetrackerservice.constants.AuthConstants;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.AuthServiceResponse;
import com.phoenix.expensetrackerservice.service.TokenValidatorService;
import com.phoenix.expensetrackerservice.utils.JsonUtils;
import org.springframework.http.RequestEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.net.URI;

@Service
public class TokenValidatorServiceImpl implements TokenValidatorService {
    private final AuthServiceClient<RequestEntity<Void>, AuthServiceResponse> authServiceClient;
    private final AuthServiceConfig authServiceConfig;

    public TokenValidatorServiceImpl(AuthServiceClient<RequestEntity<Void>, AuthServiceResponse> authServiceClient, AuthServiceConfig authServiceConfig) {
        this.authServiceClient = authServiceClient;
        this.authServiceConfig = authServiceConfig;
    }

    @Override
    public Authentication validateToken(String token) {
        String url = authServiceConfig.getAuthServiceURL();
        try {
            RequestEntity<Void> request = RequestEntity.post(URI.create(url)).header(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_PREFIX + token).build();
            AuthServiceResponse authServiceResponse = authServiceClient.sendAndReceive(request);
            String parsedResponse = JsonUtils.getJsonString(authServiceResponse);
            return new UsernamePasswordAuthenticationToken(parsedResponse, "", authServiceResponse.getAuthorities());
        } catch (JsonProcessingException jsonProcessingException) {
            throw new ExpenseTrackerException(jsonProcessingException.getMessage(), ExpenseError.SERVER_ERROR);
        } catch (RestClientException exception) {
            throw new ExpenseTrackerException("Error occurred while validating token!", ExpenseError.SERVER_ERROR, exception);
        }
    }
}
