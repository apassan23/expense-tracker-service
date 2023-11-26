package com.phoenix.expensetrackerservice.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthUtilsTest {

    private static final String USERNAME = "dummy.user";
    private static final String AUTH_PRINCIPAL = "{ \"name\": \"%s\"}".formatted(USERNAME);
    private static final String EMPTY_AUTH_PRINCIPAL = "{}";

    @Mock
    Authentication mockedAuthentication;

    @BeforeEach
    void setup() {
        SecurityContext mockedSecurityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(mockedSecurityContext);

        when(mockedSecurityContext.getAuthentication()).thenReturn(mockedAuthentication);
    }

    @Test
    void getUsernameTest() {
        when(mockedAuthentication.getPrincipal()).thenReturn(AUTH_PRINCIPAL);

        String username = AuthUtils.getUsername();
        Assertions.assertEquals(username, USERNAME);
    }

    @Test
    void getUsernameThrowsErrorTest() {
        when(mockedAuthentication.getPrincipal()).thenThrow(RuntimeException.class);

        String username = AuthUtils.getUsername();
        Assertions.assertNull(username);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = EMPTY_AUTH_PRINCIPAL)
    void getUsernameNullTest(String principal) {
        when(mockedAuthentication.getPrincipal()).thenReturn(principal);

        String username = AuthUtils.getUsername();
        Assertions.assertNull(username);
    }
}