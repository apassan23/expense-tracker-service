package com.phoenix.expensetrackerservice.agents;

import com.phoenix.expensetrackerservice.constants.IntegrationTestConstants;
import com.phoenix.expensetrackerservice.utils.CommonUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionRequestAgent {

    public static String fetchCreateTransactionRequest(String categoryId) throws IOException {
        String contents = CommonUtils.readJson(IntegrationTestConstants.CREATE_TRANSACTION_HAPPY_PATH);
        return contents.replace(IntegrationTestConstants.CATEGORY_ID_PLACEHOLDER, categoryId);
    }
}
