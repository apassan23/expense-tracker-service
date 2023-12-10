package com.phoenix.expensetrackerservice.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IntegrationTestConstants {

    public static final Integer PAGE_NUMBER = 0;
    public static final Integer PAGE_SIZE = 10;
    public static final String CREATE_TRANSACTION_HAPPY_PATH = "src/test/resources/data/CreateTransactionHappyPath.json";
    public static final String RETRIEVE_TRANSACTION_HAPPY_PATH = "src/test/resources/data/RetrieveTransactionHappyPath.json";
    public static final String CREATE_CATEGORY_HAPPY_PATH = "src/test/resources/data/CreateCategoryHappyPath.json";
    public static final String CREATE_CATEGORY_FOR_TRANSACTION_PATH = "src/test/resources/data/CreateCategoryForTransaction.json";
    public static final String CREATE_CATEGORY_FAILURE_PATH = "src/test/resources/data/CreateCategoryFailurePath.json";
    public static final String CHANGE_CATEGORY_HAPPY_PATH = "src/test/resources/data/ChangeCategoryHappyPath.json";
    public static final String CHANGE_CATEGORY_FAILURE_PATH = "src/test/resources/data/ChangeCategoryFailurePath.json";
    public static final String AUTH_SERVICE_RESPONSE_PATH = "src/test/resources/data/TokenValidateResponse.json";
    public static final String CATEGORY_ID = "categoryId";
    public static final String DATE_PLACEHOLDER = "{{date}}";
    public static final String PAGE_NUMBER_PLACEHOLDER = "{{pageNumber}}";
    public static final String PAGE_SIZE_PLACEHOLDER = "{{pageSize}}";
    public static final String CATEGORY_ID_PLACEHOLDER = String.format("{{%s}}", CATEGORY_ID);
    public static final String USERNAME = "test.user";

    public static final String CREATED_TEST_CATEGORY = "CREATED_TEST_CATEGORY";
    public static final String CREATED_CATEGORY_FOR_TRANSACTION = "CREATED_CATEGORY_FOR_TRANSACTION";
    public static final String CREATED_TRANSACTION = "CREATED_TRANSACTION";
}
