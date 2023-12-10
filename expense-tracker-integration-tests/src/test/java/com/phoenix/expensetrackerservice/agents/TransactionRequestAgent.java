package com.phoenix.expensetrackerservice.agents;

import com.phoenix.expensetrackerservice.constants.IntegrationTestConstants;
import com.phoenix.expensetrackerservice.utils.CommonUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionRequestAgent {

    public static String fetchCreateTransactionRequest(String categoryId) throws IOException {
        return CommonUtils.readJson(IntegrationTestConstants.CREATE_TRANSACTION_HAPPY_PATH)
                .replace(IntegrationTestConstants.CATEGORY_ID_PLACEHOLDER, categoryId);
    }

    public static String fetchRetrieveTransactionRequest(String date, Integer pageSize, Integer pageNumber) throws IOException {
        return CommonUtils.readJson(IntegrationTestConstants.RETRIEVE_TRANSACTION_HAPPY_PATH)
                .replace(IntegrationTestConstants.DATE_PLACEHOLDER, date)
                .replace(IntegrationTestConstants.PAGE_NUMBER_PLACEHOLDER, String.valueOf(pageNumber))
                .replace(IntegrationTestConstants.PAGE_SIZE_PLACEHOLDER, String.valueOf(pageSize));
    }

    public static String fetchRetrieveTransactionRequest() throws IOException {
        return "{}";
    }
}
