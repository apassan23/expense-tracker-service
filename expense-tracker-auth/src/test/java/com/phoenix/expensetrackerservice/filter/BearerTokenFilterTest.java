package com.phoenix.expensetrackerservice.filter;

import com.phoenix.expensetrackerservice.constants.AuthConstants;
import com.phoenix.expensetrackerservice.service.TokenValidatorService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BearerTokenFilterTest {

    private final static String TOKEN = "token";
    private final static String AUTH_HEADER = "Bearer %s".formatted(TOKEN);

    @Mock
    private TokenValidatorService tokenValidatorService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;

    private BearerTokenFilter bearerTokenFilter;

    @BeforeEach
    void setup() {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        bearerTokenFilter = spy(new BearerTokenFilter(tokenValidatorService));
    }

    @Test
    void doFilterInternalTest() throws ServletException, IOException {
        // prepare
        Authentication authentication = mock(Authentication.class);

        // stub
        when(request.getHeader(AuthConstants.AUTH_HEADER)).thenReturn(AUTH_HEADER);
        when(tokenValidatorService.validateToken(TOKEN)).thenReturn(authentication);

        // action & assert
        bearerTokenFilter.doFilterInternal(request, response, filterChain);
        Assertions.assertNotNull(SecurityContextHolder.getContext());
        verify(tokenValidatorService, times(1)).validateToken(TOKEN);
    }

    @Test
    void doFilterInternalWithNoHeaderTest() throws ServletException, IOException {
        // action & assert
        bearerTokenFilter.doFilterInternal(request, response, filterChain);
        verify(tokenValidatorService, times(0)).validateToken(TOKEN);
    }
}