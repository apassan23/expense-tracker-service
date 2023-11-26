package com.phoenix.expensetrackerservice.filter;

import com.phoenix.expensetrackerservice.constants.AuthConstants;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.service.TokenValidatorService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class BearerTokenFilter extends OncePerRequestFilter {
    private final TokenValidatorService tokenValidatorService;

    private final Logger LOGGER = LoggerFactory.getLogger(BearerTokenFilter.class);

    public BearerTokenFilter(TokenValidatorService tokenValidatorService) {
        this.tokenValidatorService = tokenValidatorService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(AuthConstants.AUTH_HEADER);
        String authToken = null;
        try {
            if (Objects.isNull(header)) {
                throw new ExpenseTrackerException("Auth Header is Missing!", ExpenseError.BAD_REQUEST);
            }

            authToken = header.replace(AuthConstants.TOKEN_PREFIX, "");
            
            Authentication authentication = tokenValidatorService.validateToken(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            LOGGER.warn("Error occurred while extracting Auth Header", e);
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }
}
