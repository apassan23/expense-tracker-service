package com.phoenix.expensetrackerservice.acceptancetests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.phoenix.expensetrackerservice.agents.CategoryRequestAgent;
import com.phoenix.expensetrackerservice.agents.TransactionRequestAgent;
import com.phoenix.expensetrackerservice.config.RunConfiguration;
import com.phoenix.expensetrackerservice.constants.ControllerConstants;
import com.phoenix.expensetrackerservice.constants.IntegrationTestConstants;
import com.phoenix.expensetrackerservice.entity.Category;
import com.phoenix.expensetrackerservice.entity.Transaction;
import com.phoenix.expensetrackerservice.model.CategoryDTO;
import com.phoenix.expensetrackerservice.model.MockedCategoryDetails;
import com.phoenix.expensetrackerservice.model.MockedTransactionDetails;
import com.phoenix.expensetrackerservice.model.TransactionDTO;
import com.phoenix.expensetrackerservice.utils.CommonUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.stream.IntStream;

@Order(2)
public class TransactionIntegrationTest extends RunConfiguration {

    @Test
    @DisplayName("Create Category")
    @Order(1)
    public void createCategoryTest() throws Exception {
        String createCategoryRequest = CategoryRequestAgent.fetchCreateCategoryForTransactionRequest();

        final String CREATE_CATEGORY_URI = String.format("%s%s", ControllerConstants.CATEGORY_CONTROLLER_REQUEST_MAPPING, ControllerConstants.CATEGORY_CREATE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.POST, CREATE_CATEGORY_URI).content(createCategoryRequest);

        enableTokenAuthentication();

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.CREATED, HttpStatus.valueOf(response.getStatus()));

        CategoryDTO categoryResponse = CommonUtils.parse(response.getContentAsString(), CategoryDTO.class);
        Assertions.assertNotNull(categoryResponse.getCategoryId());

        Category category = categoryRepository.findById(categoryResponse.getCategoryId()).orElseThrow();
        Assertions.assertEquals(category.getCategoryId(), categoryResponse.getCategoryId());
        Assertions.assertEquals(category.getGroup(), categoryResponse.getGroup());
        Assertions.assertEquals(category.getTitle(), categoryResponse.getTitle());

        // put the details in map for other tests
        MockedCategoryDetails mockedCategoryDetails = CommonUtils.buildMockedCategoryDetails(category);
        mockedCategoryDetailsMap.put(IntegrationTestConstants.CREATED_CATEGORY_FOR_TRANSACTION, mockedCategoryDetails);
    }


    @Test
    @DisplayName("Create Transaction")
    @Order(2)
    public void createTransactionTest() throws Exception {
        MockedCategoryDetails mockedCategoryDetails = mockedCategoryDetailsMap.get(IntegrationTestConstants.CREATED_CATEGORY_FOR_TRANSACTION);
        String categoryId = mockedCategoryDetails.getCategoryId();

        String createTransactionRequest = TransactionRequestAgent.fetchCreateTransactionRequest(categoryId);

        final String CREATE_TRANSACTION_URI = String.format("%s%s", ControllerConstants.TRANSACTION_CONTROLLER_REQUEST_MAPPING, ControllerConstants.TRANSACTION_CREATE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.POST, CREATE_TRANSACTION_URI)
                .content(createTransactionRequest);

        enableTokenAuthentication();

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.CREATED, HttpStatus.valueOf(response.getStatus()));

        TransactionDTO transactionResponse = CommonUtils.parse(response.getContentAsString(), TransactionDTO.class);
        Assertions.assertNotNull(transactionResponse);

        Transaction transaction = transactionRepository.findById(transactionResponse.getTransactionId()).orElseThrow();
        Assertions.assertEquals(transaction.getTransactionId(), transactionResponse.getTransactionId());
        Assertions.assertEquals(transaction.getTransactionType(), transactionResponse.getTransactionType());
        Assertions.assertEquals(transaction.getTransactionMonth(), transactionResponse.getTransactionMonth());
        Assertions.assertEquals(transaction.getTransactionDate(), transactionResponse.getTransactionDate());
        Assertions.assertEquals(transaction.getAmount(), transactionResponse.getAmount());
        Assertions.assertEquals(transaction.getTransactionName(), transactionResponse.getTransactionName());
        Assertions.assertEquals(transaction.getCategoryId(), transactionResponse.getCategoryId());
        Assertions.assertEquals(transaction.getNotes(), transactionResponse.getNotes());
        Assertions.assertEquals(IntegrationTestConstants.USERNAME, transaction.getUsername());

        // put created transaction details in a map
        MockedTransactionDetails mockedTransactionDetails = CommonUtils.buildMockedTransactionDetails(transaction);
        mockedTransactionDetailsMap.put(IntegrationTestConstants.CREATED_TRANSACTION, mockedTransactionDetails);
    }

    @Test
    @Order(3)
    @DisplayName("Retrieve Transactions")
    public void retrieveTransactionsTest() throws Exception {
        String retrieveTransactionRequest = TransactionRequestAgent.fetchRetrieveTransactionRequest();

        final String RETRIEVE_TRANSACTION_URI = String.format("%s%s", ControllerConstants.TRANSACTION_CONTROLLER_REQUEST_MAPPING, ControllerConstants.TRANSACTION_RETRIEVE_ALL_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.POST, RETRIEVE_TRANSACTION_URI)
                .content(retrieveTransactionRequest);

        enableTokenAuthentication();

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, HttpStatus.valueOf(response.getStatus()));

        List<TransactionDTO> retrieveResponse = CommonUtils.parse(response.getContentAsString(), new TypeReference<>() {
        });
        Assertions.assertNotNull(retrieveResponse);

        List<Transaction> transactions = transactionRepository.findAll();
        IntStream.range(0, transactions.size()).forEach(idx -> {

            Transaction transaction = transactions.get(idx);
            TransactionDTO retrievedTransaction = retrieveResponse.get(idx);

            Assertions.assertNotNull(transaction);
            Assertions.assertNotNull(retrievedTransaction);

            Assertions.assertEquals(transaction.getTransactionId(), retrievedTransaction.getTransactionId());
            Assertions.assertEquals(transaction.getTransactionType(), retrievedTransaction.getTransactionType());
            Assertions.assertEquals(transaction.getTransactionMonth(), retrievedTransaction.getTransactionMonth());
            Assertions.assertEquals(transaction.getTransactionDate(), retrievedTransaction.getTransactionDate());
            Assertions.assertEquals(transaction.getAmount(), retrievedTransaction.getAmount());
            Assertions.assertEquals(transaction.getTransactionName(), retrievedTransaction.getTransactionName());
            Assertions.assertEquals(transaction.getCategoryId(), retrievedTransaction.getCategoryId());
            Assertions.assertEquals(transaction.getNotes(), retrievedTransaction.getNotes());
            Assertions.assertEquals(IntegrationTestConstants.USERNAME, transaction.getUsername());
        });
    }

    @Test
    @Order(3)
    @DisplayName("Retrieve Transactions - Page Request")
    public void retrieveTransactionsWithPageRequestTest() throws Exception {
        String retrieveTransactionRequest = TransactionRequestAgent.fetchRetrieveTransactionRequest(CommonUtils.todaysDate(), IntegrationTestConstants.PAGE_SIZE, IntegrationTestConstants.PAGE_NUMBER);

        final String RETRIEVE_TRANSACTION_URI = String.format("%s%s", ControllerConstants.TRANSACTION_CONTROLLER_REQUEST_MAPPING, ControllerConstants.TRANSACTION_RETRIEVE_ALL_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.POST, RETRIEVE_TRANSACTION_URI)
                .content(retrieveTransactionRequest);

        enableTokenAuthentication();

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, HttpStatus.valueOf(response.getStatus()));

        List<TransactionDTO> retrieveResponse = CommonUtils.parse(response.getContentAsString(), new TypeReference<>() {
        });
        Assertions.assertNotNull(retrieveResponse);

        List<Transaction> transactions = transactionRepository.findAll(PageRequest.of(IntegrationTestConstants.PAGE_NUMBER, IntegrationTestConstants.PAGE_SIZE)).stream().toList();
        IntStream.range(0, transactions.size()).forEach(idx -> {

            Transaction transaction = transactions.get(idx);
            TransactionDTO retrievedTransaction = retrieveResponse.get(idx);

            Assertions.assertNotNull(transaction);
            Assertions.assertNotNull(retrievedTransaction);

            Assertions.assertEquals(transaction.getTransactionId(), retrievedTransaction.getTransactionId());
            Assertions.assertEquals(transaction.getTransactionType(), retrievedTransaction.getTransactionType());
            Assertions.assertEquals(transaction.getTransactionMonth(), retrievedTransaction.getTransactionMonth());
            Assertions.assertEquals(transaction.getTransactionDate(), retrievedTransaction.getTransactionDate());
            Assertions.assertEquals(transaction.getAmount(), retrievedTransaction.getAmount());
            Assertions.assertEquals(transaction.getTransactionName(), retrievedTransaction.getTransactionName());
            Assertions.assertEquals(transaction.getCategoryId(), retrievedTransaction.getCategoryId());
            Assertions.assertEquals(transaction.getNotes(), retrievedTransaction.getNotes());
            Assertions.assertEquals(IntegrationTestConstants.USERNAME, transaction.getUsername());
        });
    }

    @Test
    @Order(3)
    @DisplayName("Retrieve Transaction - Single Transaction")
    public void retrieveTransactionTest() throws Exception {
        MockedTransactionDetails mockedTransactionDetails = mockedTransactionDetailsMap.get(IntegrationTestConstants.CREATED_TRANSACTION);
        String transactionId = mockedTransactionDetails.getTransactionId();

        final String RETRIEVE_TRANSACTION_URI = String.format("%s%s", ControllerConstants.TRANSACTION_CONTROLLER_REQUEST_MAPPING, ControllerConstants.TRANSACTION_RETRIEVE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.GET, RETRIEVE_TRANSACTION_URI, transactionId);

        enableTokenAuthentication();

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, HttpStatus.valueOf(response.getStatus()));

        TransactionDTO retrieveResponse = CommonUtils.parse(response.getContentAsString(), TransactionDTO.class);
        Assertions.assertNotNull(retrieveResponse);

        Assertions.assertNotNull(retrieveResponse);

        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow();
        Assertions.assertNotNull(transaction);

        Assertions.assertEquals(transaction.getTransactionId(), retrieveResponse.getTransactionId());
        Assertions.assertEquals(transaction.getTransactionType(), retrieveResponse.getTransactionType());
        Assertions.assertEquals(transaction.getTransactionMonth(), retrieveResponse.getTransactionMonth());
        Assertions.assertEquals(transaction.getTransactionDate(), retrieveResponse.getTransactionDate());
        Assertions.assertEquals(transaction.getAmount(), retrieveResponse.getAmount());
        Assertions.assertEquals(transaction.getTransactionName(), retrieveResponse.getTransactionName());
        Assertions.assertEquals(transaction.getCategoryId(), retrieveResponse.getCategoryId());
        Assertions.assertEquals(transaction.getNotes(), retrieveResponse.getNotes());
        Assertions.assertEquals(IntegrationTestConstants.USERNAME, transaction.getUsername());

    }
}
