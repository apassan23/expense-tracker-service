package com.phoenix.expensetrackerservice.client.impl;

import com.phoenix.expensetrackerservice.constants.ErrorConstants;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.AuthServiceResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthServiceClientImplTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private RequestEntity<Void> request;
    @Mock
    ResponseEntity<AuthServiceResponse> responseEntity;

    private AuthServiceClientImpl authServiceClient;

    @BeforeEach
    void setup() {
        when(request.getUrl()).thenReturn(URI.create("http://localhost"));
        when(request.getMethod()).thenReturn(HttpMethod.POST);

        authServiceClient = spy(new AuthServiceClientImpl(restTemplate));
    }

    @Test
    void sendAndReceiveTest() {
        // prepare
        AuthServiceResponse authServiceResponse = new AuthServiceResponse();

        // stub
        when(restTemplate.exchange(any(URI.class), any(HttpMethod.class), eq(request), eq(AuthServiceResponse.class))).thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(authServiceResponse);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);

        // action & assert
        AuthServiceResponse response = authServiceClient.sendAndReceive(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(authServiceResponse, response);
    }

    @Test
    void sendAndReceiveErrorResponseTest() {
        // stub
        when(restTemplate.exchange(any(URI.class), any(HttpMethod.class), eq(request), eq(AuthServiceResponse.class))).thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(null);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);

        // action & assert
        ExpenseTrackerException exception = Assertions.assertThrows(ExpenseTrackerException.class, () -> authServiceClient.sendAndReceive(request));
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertNotNull(exception.getExpenseError());
        Assertions.assertEquals(ErrorConstants.TOKEN_VALIDATION_ERROR, exception.getMessage());
        Assertions.assertEquals(ExpenseError.SERVER_ERROR, exception.getExpenseError());
    }

    @Test
    void sendAndReceiveThrowsExceptionTest() {
        // stub
        when(restTemplate.exchange(any(URI.class), any(HttpMethod.class), eq(request), eq(AuthServiceResponse.class))).thenThrow(RestClientException.class);
        when(responseEntity.getBody()).thenReturn(null);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);

        // action & assert
        ExpenseTrackerException exception = Assertions.assertThrows(ExpenseTrackerException.class, () -> authServiceClient.sendAndReceive(request));
        Assertions.assertNotNull(exception.getExpenseError());
        Assertions.assertNotEquals(ErrorConstants.TOKEN_VALIDATION_ERROR, exception.getMessage());
        Assertions.assertEquals(ExpenseError.SERVER_ERROR, exception.getExpenseError());
    }
}