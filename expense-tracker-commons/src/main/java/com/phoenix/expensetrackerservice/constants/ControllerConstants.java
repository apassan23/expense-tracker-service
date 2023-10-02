package com.phoenix.expensetrackerservice.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ControllerConstants {
    public static final String TRANSACTION_CONTROLLER_REQUEST_MAPPING = "/v1/transaction";
    public static final String CATEGORY_CONTROLLER_REQUEST_MAPPING = "/v1/category";
    public static final String TRANSACTION_CREATE_MAPPING = "/";
    public static final String TRANSACTION_RETRIEVE_ALL_MAPPING = "/all";
    public static final String TRANSACTION_CHANGE_MAPPING = "/";
    public static final String TRANSACTION_RETRIEVE_MAPPING = "/{transactionId}";
    public static final String TRANSACTION_DELETE_MAPPING = "/{transactionId}";
    public static final String CATEGORY_CREATE_MAPPING = "/";
    public static final String CATEGORY_RETRIEVE_ALL_MAPPING = "/all";
    public static final String CATEGORY_CHANGE_MAPPING = "/";
    public static final String CATEGORY_RETRIEVE_MAPPING = "/{categoryId}";
    public static final String CATEGORY_DELETE_MAPPING = "/{categoryId}";
    public static final String ACTUATOR_INFO_ENDPOINT = "/actuator/info";
    public static final String ACTUATOR_HEALTH_ENDPOINT = "/actuator/health";
}
