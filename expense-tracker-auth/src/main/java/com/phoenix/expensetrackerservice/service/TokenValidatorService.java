package com.phoenix.expensetrackerservice.service;

import org.springframework.security.core.Authentication;

public interface TokenValidatorService {
    Authentication validateToken(String token);
}
