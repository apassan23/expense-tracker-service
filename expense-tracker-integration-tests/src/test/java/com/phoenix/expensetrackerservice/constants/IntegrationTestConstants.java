package com.phoenix.expensetrackerservice.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IntegrationTestConstants {

    public static final String CREATE_TRANSACTION_HAPPY_PATH = "src/test/resources/data/CreateTransactionHappyPath.json";
    public static final String CREATE_CATEGORY_HAPPY_PATH = "src/test/resources/data/CreateCategoryHappyPath.json";
    public static final String CREATE_CATEGORY_FAILURE_PATH = "src/test/resources/data/CreateCategoryFailurePath.json";
    public static final String CHANGE_CATEGORY_HAPPY_PATH = "src/test/resources/data/ChangeCategoryHappyPath.json";
    public static final String CHANGE_CATEGORY_FAILURE_PATH = "src/test/resources/data/ChangeCategoryFailurePath.json";
    public static final String AUTH_SERVICE_RESPONSE_PATH = "src/test/resources/data/TokenValidateResponse.json";
    public static final String CATEGORY_ID = "categoryId";
    public static final String CATEGORY_ID_PLACEHOLDER = String.format("{{%s}}", CATEGORY_ID);

    public static final String CREATED_TEST_CATEGORY = "CREATED_TEST_CATEGORY";
}
