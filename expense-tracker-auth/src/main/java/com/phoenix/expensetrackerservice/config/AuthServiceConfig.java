package com.phoenix.expensetrackerservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@Getter
public class AuthServiceConfig {
    @Value("${auth-service.url}")
    private String authServiceURL;

    @Bean
    public RestTemplate authRestTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.of(2L, ChronoUnit.SECONDS))
                .build();
    }
}
