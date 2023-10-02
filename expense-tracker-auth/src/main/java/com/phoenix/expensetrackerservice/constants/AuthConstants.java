package com.phoenix.expensetrackerservice.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthConstants {

    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String[] API_WHITELIST = {
            ControllerConstants.ACTUATOR_INFO_ENDPOINT,
            ControllerConstants.ACTUATOR_HEALTH_ENDPOINT
    };
}
