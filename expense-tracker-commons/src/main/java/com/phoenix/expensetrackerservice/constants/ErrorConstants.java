package com.phoenix.expensetrackerservice.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorConstants {
    public static final String BAD_REQUEST_MESSAGE = "Request was malformed.";
    public static final String CATEGORY_ALREADY_EXISTS_MESSAGE = "Category with the same name already exists.";
    public static final String USERNAME_NULL_MESSAGE = "Username is null!";
    public static final String TOKEN_VALIDATION_ERROR = "Error occurred while validating token";
}
