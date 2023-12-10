package com.phoenix.expensetrackerservice.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WiremockConstants {
    public static final Integer PORT = 8080;
    public static final String AUTH_SERVICE_URI = "/v1/tokens/validate";
    public static final String CONTENT_TYPE = "Content-Type";
}
