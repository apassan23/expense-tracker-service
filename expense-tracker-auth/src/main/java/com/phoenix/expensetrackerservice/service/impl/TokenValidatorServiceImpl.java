package com.phoenix.expensetrackerservice.service.impl;

import com.phoenix.expensetrackerservice.client.AuthServiceClient;
import com.phoenix.expensetrackerservice.config.AuthServiceConfig;
import com.phoenix.expensetrackerservice.constants.AuthConstants;
import com.phoenix.expensetrackerservice.model.AuthServiceResponse;
import com.phoenix.expensetrackerservice.service.TokenValidatorService;
import org.springframework.http.RequestEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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
        RequestEntity<Void> request = RequestEntity.post(URI.create(url)).header(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_PREFIX + token).build();
        AuthServiceResponse authServiceResponse = authServiceClient.sendAndReceive(request);
        return new UsernamePasswordAuthenticationToken(authServiceResponse, "", authServiceResponse.getAuthorities());
    }
}
