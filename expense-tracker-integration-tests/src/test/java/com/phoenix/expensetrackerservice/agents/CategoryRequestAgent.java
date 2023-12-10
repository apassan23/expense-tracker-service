package com.phoenix.expensetrackerservice.agents;

import com.phoenix.expensetrackerservice.constants.IntegrationTestConstants;
import com.phoenix.expensetrackerservice.utils.CommonUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryRequestAgent {

    public static String fetchCreateCategoryRequest() throws IOException {
        return CommonUtils.readJson(IntegrationTestConstants.CREATE_CATEGORY_HAPPY_PATH);
    }

    public static String fetchCreateCategoryFailureRequest() throws IOException {
        return CommonUtils.readJson(IntegrationTestConstants.CREATE_CATEGORY_FAILURE_PATH);
    }

    public static String fetchChangeCategoryRequest(String categoryId) throws IOException {
        return CommonUtils.readJson(IntegrationTestConstants.CHANGE_CATEGORY_HAPPY_PATH)
                .replace(IntegrationTestConstants.CATEGORY_ID_PLACEHOLDER, categoryId);
    }

    public static String fetchChangeCategoryFailureRequest(String categoryId) throws IOException {
        return CommonUtils.readJson(IntegrationTestConstants.CHANGE_CATEGORY_FAILURE_PATH)
                .replace(IntegrationTestConstants.CATEGORY_ID_PLACEHOLDER, categoryId);
    }
}
