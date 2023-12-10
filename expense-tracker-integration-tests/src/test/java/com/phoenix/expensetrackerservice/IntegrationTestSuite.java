package com.phoenix.expensetrackerservice;

import com.phoenix.expensetrackerservice.acceptancetests.CategoryIntegrationTest;
import com.phoenix.expensetrackerservice.acceptancetests.TransactionIntegrationTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Expense Tracker Integration Test Suite")
@SelectClasses({CategoryIntegrationTest.class, TransactionIntegrationTest.class})
public class IntegrationTestSuite {
}
