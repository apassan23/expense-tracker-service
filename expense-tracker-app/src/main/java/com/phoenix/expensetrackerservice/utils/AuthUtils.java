package com.phoenix.expensetrackerservice.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthUtils {

    private static final String NAME = "name";

    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            if (Objects.nonNull(authentication) && Objects.nonNull(authentication.getPrincipal())) {

                Object principal = authentication.getPrincipal();
                Map<String, Object> authDetails = JsonUtils.parse(String.valueOf(principal));

                Object username = authDetails.get(NAME);
                if (Objects.nonNull(username)) {
                    return String.valueOf(username);
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
