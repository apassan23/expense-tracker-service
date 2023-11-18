package com.phoenix.expensetrackerservice.client.impl;

import com.phoenix.expensetrackerservice.client.AuthServiceClient;
import com.phoenix.expensetrackerservice.constants.ErrorConstants;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.AuthServiceResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Objects;

@Component
public class AuthServiceClientImpl implements AuthServiceClient<RequestEntity<Void>, AuthServiceResponse> {
    private final RestTemplate authRestTemplate;

    public AuthServiceClientImpl(RestTemplate authRestTemplate) {
        this.authRestTemplate = authRestTemplate;
    }

    @Override
    public AuthServiceResponse sendAndReceive(RequestEntity<Void> request) {
        try {
            URI uri = request.getUrl();
            HttpMethod method = request.getMethod();
            ResponseEntity<AuthServiceResponse> authServiceResponseResponseEntity = authRestTemplate.exchange(uri, method, request, AuthServiceResponse.class);
            if (Objects.nonNull(authServiceResponseResponseEntity.getBody()) && !authServiceResponseResponseEntity.getStatusCode().isError()) {
                return authServiceResponseResponseEntity.getBody();
            }
            throw new ExpenseTrackerException(ErrorConstants.TOKEN_VALIDATION_ERROR, ExpenseError.SERVER_ERROR);
        } catch (ExpenseTrackerException e) {
            throw e;
        } catch (Exception e) {
            throw new ExpenseTrackerException(e.getMessage(), ExpenseError.SERVER_ERROR);
        }
    }
}
