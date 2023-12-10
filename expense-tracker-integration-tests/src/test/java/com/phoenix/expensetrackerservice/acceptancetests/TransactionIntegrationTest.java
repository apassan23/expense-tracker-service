package com.phoenix.expensetrackerservice.acceptancetests;

import com.phoenix.expensetrackerservice.agents.TransactionRequestAgent;
import com.phoenix.expensetrackerservice.config.RunConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@Order(2)
public class TransactionIntegrationTest extends RunConfiguration {

    @Test
    @DisplayName("Create Transaction")
    public void createTransactionTest() throws IOException {
        String createTransactionRequest = TransactionRequestAgent.fetchCreateTransactionRequest("");
    }
}
