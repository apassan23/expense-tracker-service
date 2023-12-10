package com.phoenix.expensetrackerservice.acceptancetests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.phoenix.expensetrackerservice.agents.CategoryRequestAgent;
import com.phoenix.expensetrackerservice.agents.TransactionRequestAgent;
import com.phoenix.expensetrackerservice.config.RunConfiguration;
import com.phoenix.expensetrackerservice.constants.ControllerConstants;
import com.phoenix.expensetrackerservice.constants.ErrorConstants;
import com.phoenix.expensetrackerservice.constants.IntegrationTestConstants;
import com.phoenix.expensetrackerservice.entity.Category;
import com.phoenix.expensetrackerservice.entity.Transaction;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.exception.response.ErrorType;
import com.phoenix.expensetrackerservice.exception.response.ExpenseTrackerError;
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
    @DisplayName("Create Transaction - Transaction Already Exists")
    @Order(3)
    public void createTransactionAlreadyExistsTest() throws Exception {
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
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, HttpStatus.valueOf(response.getStatus()));

        ExpenseTrackerError errorResponse = CommonUtils.parse(response.getContentAsString(), ExpenseTrackerError.class);
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(ExpenseError.TRANSACTION_CREATE_ALREADY_EXISTS, errorResponse.getErrorCode());
        Assertions.assertEquals(ErrorType.BAD_REQUEST, errorResponse.getErrorType());
        Assertions.assertEquals(ExpenseError.TRANSACTION_CREATE_ALREADY_EXISTS.getDescription(), errorResponse.getErrorDescription());
    }

    @Test
    @DisplayName("Create Transaction - Invalid Auth Response (No username)")
    @Order(2)
    public void createTransactionInvalidAuthResponseTest() throws Exception {
        MockedCategoryDetails mockedCategoryDetails = mockedCategoryDetailsMap.get(IntegrationTestConstants.CREATED_CATEGORY_FOR_TRANSACTION);
        String categoryId = mockedCategoryDetails.getCategoryId();

        String createTransactionRequest = TransactionRequestAgent.fetchCreateTransactionRequest(categoryId);

        final String CREATE_TRANSACTION_URI = String.format("%s%s", ControllerConstants.TRANSACTION_CONTROLLER_REQUEST_MAPPING, ControllerConstants.TRANSACTION_CREATE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.POST, CREATE_TRANSACTION_URI)
                .content(createTransactionRequest);

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, HttpStatus.valueOf(response.getStatus()));

        ExpenseTrackerError errorResponse = CommonUtils.parse(response.getContentAsString(), ExpenseTrackerError.class);
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(ExpenseError.BAD_REQUEST, errorResponse.getErrorCode());
        Assertions.assertEquals(ErrorType.BAD_REQUEST, errorResponse.getErrorType());
    }

    @Test
    @DisplayName("Create Transaction - Validation Error")
    @Order(2)
    public void createTransactionValidationErrorTest() throws Exception {
        MockedCategoryDetails mockedCategoryDetails = mockedCategoryDetailsMap.get(IntegrationTestConstants.CREATED_CATEGORY_FOR_TRANSACTION);
        String categoryId = mockedCategoryDetails.getCategoryId();

        String createTransactionRequest = TransactionRequestAgent.fetchCreateTransactionFailureRequest(categoryId);

        final String CREATE_TRANSACTION_URI = String.format("%s%s", ControllerConstants.TRANSACTION_CONTROLLER_REQUEST_MAPPING, ControllerConstants.TRANSACTION_CREATE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.POST, CREATE_TRANSACTION_URI)
                .content(createTransactionRequest);

        enableTokenAuthentication();

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, HttpStatus.valueOf(response.getStatus()));

        ExpenseTrackerError errorResponse = CommonUtils.parse(response.getContentAsString(), ExpenseTrackerError.class);
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(ExpenseError.BAD_REQUEST, errorResponse.getErrorCode());
        Assertions.assertEquals(ErrorType.BAD_REQUEST, errorResponse.getErrorType());
        Assertions.assertEquals(ErrorConstants.BAD_REQUEST_MESSAGE, errorResponse.getErrorDescription());
    }

    @Test
    @Order(3)
    @DisplayName("Retrieve Transactions - Invalid Auth Response (No username)")
    public void retrieveTransactionsInvalidAuthResponseTest() throws Exception {
        String retrieveTransactionRequest = TransactionRequestAgent.fetchRetrieveTransactionRequest();

        final String RETRIEVE_TRANSACTION_URI = String.format("%s%s", ControllerConstants.TRANSACTION_CONTROLLER_REQUEST_MAPPING, ControllerConstants.TRANSACTION_RETRIEVE_ALL_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.POST, RETRIEVE_TRANSACTION_URI)
                .content(retrieveTransactionRequest);

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, HttpStatus.valueOf(response.getStatus()));

        ExpenseTrackerError errorResponse = CommonUtils.parse(response.getContentAsString(), ExpenseTrackerError.class);
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(ExpenseError.BAD_REQUEST, errorResponse.getErrorCode());
        Assertions.assertEquals(ErrorType.BAD_REQUEST, errorResponse.getErrorType());
    }

    @Test
    @Order(3)
    @DisplayName("Retrieve Transactions - Validation Error")
    public void retrieveTransactionsValidationErrorTest() throws Exception {
        String retrieveTransactionRequest = TransactionRequestAgent.fetchRetrieveTransactionFailureRequest(IntegrationTestConstants.PAGE_SIZE, IntegrationTestConstants.PAGE_NUMBER);

        final String RETRIEVE_TRANSACTION_URI = String.format("%s%s", ControllerConstants.TRANSACTION_CONTROLLER_REQUEST_MAPPING, ControllerConstants.TRANSACTION_RETRIEVE_ALL_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.POST, RETRIEVE_TRANSACTION_URI)
                .content(retrieveTransactionRequest);

        enableTokenAuthentication();

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, HttpStatus.valueOf(response.getStatus()));

        ExpenseTrackerError errorResponse = CommonUtils.parse(response.getContentAsString(), ExpenseTrackerError.class);
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(ExpenseError.BAD_REQUEST, errorResponse.getErrorCode());
        Assertions.assertEquals(ErrorType.BAD_REQUEST, errorResponse.getErrorType());
        Assertions.assertEquals(ErrorConstants.BAD_REQUEST_MESSAGE, errorResponse.getErrorDescription());
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
    @DisplayName("Retrieve Transactions -- Page Request - Invalid Date")
    public void retrieveTransactionsWithPageRequestInvalidDateTest() throws Exception {
        String retrieveTransactionRequest = TransactionRequestAgent.fetchRetrieveTransactionRequest("", IntegrationTestConstants.PAGE_SIZE, IntegrationTestConstants.PAGE_NUMBER);

        final String RETRIEVE_TRANSACTION_URI = String.format("%s%s", ControllerConstants.TRANSACTION_CONTROLLER_REQUEST_MAPPING, ControllerConstants.TRANSACTION_RETRIEVE_ALL_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.POST, RETRIEVE_TRANSACTION_URI)
                .content(retrieveTransactionRequest);

        enableTokenAuthentication();

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.valueOf(response.getStatus()));

        ExpenseTrackerError errorResponse = CommonUtils.parse(response.getContentAsString(), ExpenseTrackerError.class);
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(ExpenseError.SERVER_ERROR, errorResponse.getErrorCode());
        Assertions.assertEquals(ErrorType.INTERNAL_SERVER_ERROR, errorResponse.getErrorType());
    }

    @Test
    @Order(3)
    @DisplayName("Retrieve Transactions - Page Request (Invalid Auth Response)")
    public void retrieveTransactionsWithPageRequestValidationErrorTest() throws Exception {
        String retrieveTransactionRequest = TransactionRequestAgent.fetchRetrieveTransactionRequest(CommonUtils.todaysDate(), IntegrationTestConstants.PAGE_SIZE, IntegrationTestConstants.PAGE_NUMBER);

        final String RETRIEVE_TRANSACTION_URI = String.format("%s%s", ControllerConstants.TRANSACTION_CONTROLLER_REQUEST_MAPPING, ControllerConstants.TRANSACTION_RETRIEVE_ALL_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.POST, RETRIEVE_TRANSACTION_URI)
                .content(retrieveTransactionRequest);

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, HttpStatus.valueOf(response.getStatus()));

        ExpenseTrackerError errorResponse = CommonUtils.parse(response.getContentAsString(), ExpenseTrackerError.class);
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(ExpenseError.BAD_REQUEST, errorResponse.getErrorCode());
        Assertions.assertEquals(ErrorType.BAD_REQUEST, errorResponse.getErrorType());
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

    @Test
    @Order(3)
    @DisplayName("Retrieve Transaction - Transaction Does Not Exists")
    public void retrieveTransactionDoesNotExistsTest() throws Exception {
        String transactionId = CommonUtils.generateUUID();

        final String RETRIEVE_TRANSACTION_URI = String.format("%s%s", ControllerConstants.TRANSACTION_CONTROLLER_REQUEST_MAPPING, ControllerConstants.TRANSACTION_RETRIEVE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.GET, RETRIEVE_TRANSACTION_URI, transactionId);

        enableTokenAuthentication();

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, HttpStatus.valueOf(response.getStatus()));

        ExpenseTrackerError errorResponse = CommonUtils.parse(response.getContentAsString(), ExpenseTrackerError.class);
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(ExpenseError.TRANSACTION_NOT_PRESENT, errorResponse.getErrorCode());
        Assertions.assertEquals(ErrorType.NOT_FOUND_ERROR, errorResponse.getErrorType());
    }

    @Test
    @Order(4)
    @DisplayName("Change Transaction")
    public void changeTransactionTest() throws Exception {
        MockedTransactionDetails mockedTransactionDetails = mockedTransactionDetailsMap.get(IntegrationTestConstants.CREATED_TRANSACTION);
        String transactionId = mockedTransactionDetails.getTransactionId();
        String categoryId = mockedTransactionDetails.getCategoryId();

        String changeTransactionRequest = TransactionRequestAgent.fetchChangeTransactionRequest(transactionId, categoryId);

        final String CHANGE_REQUEST_URI = String.format("%s%s", ControllerConstants.TRANSACTION_CONTROLLER_REQUEST_MAPPING, ControllerConstants.TRANSACTION_CHANGE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.PUT, CHANGE_REQUEST_URI)
                .content(changeTransactionRequest);

        enableTokenAuthentication();

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.ACCEPTED, HttpStatus.valueOf(response.getStatus()));

        TransactionDTO changeResponse = CommonUtils.parse(response.getContentAsString(), TransactionDTO.class);
        Assertions.assertNotNull(changeResponse);

        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow();
        Assertions.assertNotNull(transaction);
        Assertions.assertEquals(transaction.getTransactionId(), changeResponse.getTransactionId());
        Assertions.assertEquals(transaction.getTransactionType(), changeResponse.getTransactionType());
        Assertions.assertEquals(transaction.getTransactionMonth(), changeResponse.getTransactionMonth());
        Assertions.assertEquals(transaction.getTransactionDate(), changeResponse.getTransactionDate());
        Assertions.assertEquals(transaction.getAmount(), changeResponse.getAmount());
        Assertions.assertEquals(transaction.getTransactionName(), changeResponse.getTransactionName());
        Assertions.assertEquals(transaction.getCategoryId(), changeResponse.getCategoryId());
        Assertions.assertEquals(transaction.getNotes(), changeResponse.getNotes());
        Assertions.assertEquals(IntegrationTestConstants.USERNAME, transaction.getUsername());
    }

    @Test
    @Order(4)
    @DisplayName("Change Transaction - Transaction Does Not Exists")
    public void changeTransactionDoesNotExistsTest() throws Exception {
        MockedTransactionDetails mockedTransactionDetails = mockedTransactionDetailsMap.get(IntegrationTestConstants.CREATED_TRANSACTION);
        String transactionId = CommonUtils.generateUUID();
        String categoryId = mockedTransactionDetails.getCategoryId();

        String changeTransactionRequest = TransactionRequestAgent.fetchChangeTransactionRequest(transactionId, categoryId);

        final String CHANGE_REQUEST_URI = String.format("%s%s", ControllerConstants.TRANSACTION_CONTROLLER_REQUEST_MAPPING, ControllerConstants.TRANSACTION_CHANGE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.PUT, CHANGE_REQUEST_URI)
                .content(changeTransactionRequest);

        enableTokenAuthentication();

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, HttpStatus.valueOf(response.getStatus()));

        ExpenseTrackerError errorResponse = CommonUtils.parse(response.getContentAsString(), ExpenseTrackerError.class);
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(ExpenseError.TRANSACTION_NOT_PRESENT, errorResponse.getErrorCode());
        Assertions.assertEquals(ErrorType.NOT_FOUND_ERROR, errorResponse.getErrorType());
    }

    @Test
    @Order(4)
    @DisplayName("Change Transaction - Category Does Not Exists")
    public void changeTransactionWithCategoryDoesNotExistsTest() throws Exception {
        MockedTransactionDetails mockedTransactionDetails = mockedTransactionDetailsMap.get(IntegrationTestConstants.CREATED_TRANSACTION);
        String transactionId = mockedTransactionDetails.getTransactionId();
        String categoryId = CommonUtils.generateUUID();

        String changeTransactionRequest = TransactionRequestAgent.fetchChangeTransactionRequest(transactionId, categoryId);

        final String CHANGE_REQUEST_URI = String.format("%s%s", ControllerConstants.TRANSACTION_CONTROLLER_REQUEST_MAPPING, ControllerConstants.TRANSACTION_CHANGE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.PUT, CHANGE_REQUEST_URI)
                .content(changeTransactionRequest);

        enableTokenAuthentication();

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, HttpStatus.valueOf(response.getStatus()));

        ExpenseTrackerError errorResponse = CommonUtils.parse(response.getContentAsString(), ExpenseTrackerError.class);
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(ExpenseError.CATEGORY_DOES_NOT_EXISTS, errorResponse.getErrorCode());
        Assertions.assertEquals(ErrorType.NOT_FOUND_ERROR, errorResponse.getErrorType());
    }

    @Test
    @Order(4)
    @DisplayName("Change Transaction - Invalid Auth Response (No username)")
    public void changeTransactionInvalidAuthResponseTest() throws Exception {
        MockedTransactionDetails mockedTransactionDetails = mockedTransactionDetailsMap.get(IntegrationTestConstants.CREATED_TRANSACTION);
        String transactionId = mockedTransactionDetails.getTransactionId();
        String categoryId = mockedTransactionDetails.getCategoryId();

        String changeTransactionRequest = TransactionRequestAgent.fetchChangeTransactionRequest(transactionId, categoryId);

        final String CHANGE_REQUEST_URI = String.format("%s%s", ControllerConstants.TRANSACTION_CONTROLLER_REQUEST_MAPPING, ControllerConstants.TRANSACTION_CHANGE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.PUT, CHANGE_REQUEST_URI)
                .content(changeTransactionRequest);

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, HttpStatus.valueOf(response.getStatus()));

        ExpenseTrackerError errorResponse = CommonUtils.parse(response.getContentAsString(), ExpenseTrackerError.class);
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(ExpenseError.BAD_REQUEST, errorResponse.getErrorCode());
        Assertions.assertEquals(ErrorType.BAD_REQUEST, errorResponse.getErrorType());
    }

    @Test
    @Order(4)
    @DisplayName("Change Transaction - Validation Error")
    public void changeTransactionValidationErrorTest() throws Exception {
        MockedTransactionDetails mockedTransactionDetails = mockedTransactionDetailsMap.get(IntegrationTestConstants.CREATED_TRANSACTION);
        String categoryId = mockedTransactionDetails.getCategoryId();

        String changeTransactionRequest = TransactionRequestAgent.fetchChangeTransactionFailureRequest(categoryId);

        final String CHANGE_REQUEST_URI = String.format("%s%s", ControllerConstants.TRANSACTION_CONTROLLER_REQUEST_MAPPING, ControllerConstants.TRANSACTION_CHANGE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.PUT, CHANGE_REQUEST_URI)
                .content(changeTransactionRequest);

        enableTokenAuthentication();

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, HttpStatus.valueOf(response.getStatus()));

        ExpenseTrackerError errorResponse = CommonUtils.parse(response.getContentAsString(), ExpenseTrackerError.class);
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(ExpenseError.BAD_REQUEST, errorResponse.getErrorCode());
        Assertions.assertEquals(ErrorType.BAD_REQUEST, errorResponse.getErrorType());
        Assertions.assertEquals(ErrorConstants.BAD_REQUEST_MESSAGE, errorResponse.getErrorDescription());
    }

    @Test
    @DisplayName("Delete Transaction")
    public void deleteTransactionTest() throws Exception {
        MockedTransactionDetails mockedTransactionDetails = mockedTransactionDetailsMap.get(IntegrationTestConstants.CREATED_TRANSACTION);
        String transactionId = mockedTransactionDetails.getTransactionId();

        final String DELETE_TRANSACTION_URI = String.format("%s%s", ControllerConstants.TRANSACTION_CONTROLLER_REQUEST_MAPPING, ControllerConstants.TRANSACTION_DELETE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.DELETE, DELETE_TRANSACTION_URI, transactionId);

        enableTokenAuthentication();

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.ACCEPTED, HttpStatus.valueOf(response.getStatus()));

        Transaction transaction = transactionRepository.findById(transactionId).orElse(null);
        Assertions.assertNull(transaction);
    }

    @Test
    @DisplayName("Delete Transaction - Transaction Does Not Exists")
    public void deleteTransactionDoesNotExistsTest() throws Exception {
        String transactionId = CommonUtils.generateUUID();

        final String DELETE_TRANSACTION_URI = String.format("%s%s", ControllerConstants.TRANSACTION_CONTROLLER_REQUEST_MAPPING, ControllerConstants.TRANSACTION_DELETE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.DELETE, DELETE_TRANSACTION_URI, transactionId);

        enableTokenAuthentication();

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, HttpStatus.valueOf(response.getStatus()));

        ExpenseTrackerError errorResponse = CommonUtils.parse(response.getContentAsString(), ExpenseTrackerError.class);
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(ExpenseError.TRANSACTION_NOT_PRESENT, errorResponse.getErrorCode());
        Assertions.assertEquals(ErrorType.NOT_FOUND_ERROR, errorResponse.getErrorType());
    }

    @Test
    @DisplayName("Delete Transaction - Invalid Auth Response (No username)")
    public void deleteTransactionInvalidAuthResponseTest() throws Exception {
        MockedTransactionDetails mockedTransactionDetails = mockedTransactionDetailsMap.get(IntegrationTestConstants.CREATED_TRANSACTION);
        String transactionId = mockedTransactionDetails.getTransactionId();

        final String DELETE_TRANSACTION_URI = String.format("%s%s", ControllerConstants.TRANSACTION_CONTROLLER_REQUEST_MAPPING, ControllerConstants.TRANSACTION_DELETE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.DELETE, DELETE_TRANSACTION_URI, transactionId);

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, HttpStatus.valueOf(response.getStatus()));

        ExpenseTrackerError errorResponse = CommonUtils.parse(response.getContentAsString(), ExpenseTrackerError.class);
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(ExpenseError.BAD_REQUEST, errorResponse.getErrorCode());
        Assertions.assertEquals(ErrorType.BAD_REQUEST, errorResponse.getErrorType());
    }
}
