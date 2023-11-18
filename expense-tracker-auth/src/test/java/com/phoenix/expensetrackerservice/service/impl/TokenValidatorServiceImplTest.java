package com.phoenix.expensetrackerservice.service.impl;

import com.phoenix.expensetrackerservice.client.impl.AuthServiceClientImpl;
import com.phoenix.expensetrackerservice.config.AuthServiceConfig;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
import com.phoenix.expensetrackerservice.model.AuthServiceResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenValidatorServiceImplTest {

    @Mock
    private AuthServiceClientImpl authServiceClient;
    @Mock
    private AuthServiceConfig authServiceConfig;

    private TokenValidatorServiceImpl tokenValidatorService;

    @BeforeEach
    void setup() {
        tokenValidatorService = spy(new TokenValidatorServiceImpl(authServiceClient, authServiceConfig));
    }

    @Test
    void validateTokenTest() {
        // prepare
        AuthServiceResponse authServiceResponse = new AuthServiceResponse();
        authServiceResponse.setName("dummy");
        authServiceResponse.setAuthorities(Set.of());

        // stub
        when(authServiceConfig.getAuthServiceURL()).thenReturn("http://localhost:8080/auth-service");
        when(authServiceClient.sendAndReceive(any())).thenReturn(authServiceResponse);

        // action & assert
        Authentication authentication = tokenValidatorService.validateToken("token");
        Assertions.assertNotNull(authentication);
        Assertions.assertNotNull(authentication.getPrincipal());
        Assertions.assertNotNull(authentication.getCredentials());
        Assertions.assertNotNull(authentication.getAuthorities());
    }

    @Test
    void validateTokenThrowsHttpClientErrorExceptionTest() {
        // prepare
        AuthServiceResponse authServiceResponse = new AuthServiceResponse();
        authServiceResponse.setName("dummy");
        authServiceResponse.setAuthorities(Set.of());

        // stub
        when(authServiceConfig.getAuthServiceURL()).thenReturn("http://localhost:8080/auth-service");
        when(authServiceClient.sendAndReceive(any())).thenThrow(HttpClientErrorException.class);

        // action & assert
        Assertions.assertThrows(ExpenseTrackerException.class, () -> tokenValidatorService.validateToken("token"));
    }
}