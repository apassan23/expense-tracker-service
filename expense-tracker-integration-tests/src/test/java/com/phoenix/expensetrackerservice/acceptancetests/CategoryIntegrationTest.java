package com.phoenix.expensetrackerservice.acceptancetests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.phoenix.expensetrackerservice.agents.CategoryRequestAgent;
import com.phoenix.expensetrackerservice.config.RunConfiguration;
import com.phoenix.expensetrackerservice.constants.ControllerConstants;
import com.phoenix.expensetrackerservice.constants.ErrorConstants;
import com.phoenix.expensetrackerservice.constants.IntegrationTestConstants;
import com.phoenix.expensetrackerservice.entity.Category;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.exception.response.ErrorType;
import com.phoenix.expensetrackerservice.exception.response.ExpenseTrackerError;
import com.phoenix.expensetrackerservice.model.CategoryDTO;
import com.phoenix.expensetrackerservice.model.MockedCategoryDetails;
import com.phoenix.expensetrackerservice.utils.CommonUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

@Order(1)
public class CategoryIntegrationTest extends RunConfiguration {

    private static final String CHANGED_TITLE = "changedTitle";
    private static final String TEST_TITLE = "testTitle";

    @Test
    @DisplayName("Create Category")
    @Order(1)
    public void createCategoryTest() throws Exception {
        String createCategoryRequest = CategoryRequestAgent.fetchCreateCategoryRequest();

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
        mockedCategoryDetailsMap.put(IntegrationTestConstants.CREATED_TEST_CATEGORY, mockedCategoryDetails);
    }

    @Test
    @DisplayName("Create Category - Validation Failure")
    @Order(1)
    public void createCategoryValidationFailureTest() throws Exception {
        String createCategoryRequest = CategoryRequestAgent.fetchCreateCategoryFailureRequest();

        final String CREATE_CATEGORY_URI = String.format("%s%s", ControllerConstants.CATEGORY_CONTROLLER_REQUEST_MAPPING, ControllerConstants.CATEGORY_CREATE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.POST, CREATE_CATEGORY_URI).content(createCategoryRequest);

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
        Assertions.assertEquals(ExpenseError.BAD_REQUEST.getDescription(), errorResponse.getErrorDescription());
    }

    @Test
    @DisplayName("Create Category - Invalid Auth Response (No username)")
    @Order(2)
    public void createCategoryInvalidAuthResponseTest() throws Exception {
        String createCategoryRequest = CategoryRequestAgent.fetchCreateCategoryFailureRequest();

        final String CREATE_CATEGORY_URI = String.format("%s%s", ControllerConstants.CATEGORY_CONTROLLER_REQUEST_MAPPING, ControllerConstants.CATEGORY_CREATE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.POST, CREATE_CATEGORY_URI).content(createCategoryRequest);

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
    @DisplayName("Create Category - Category Already Exists")
    @Order(2)
    public void createCategoryAlreadyExistingTest() throws Exception {
        String createCategoryRequest = CategoryRequestAgent.fetchCreateCategoryRequest();

        final String CREATE_CATEGORY_URI = String.format("%s%s", ControllerConstants.CATEGORY_CONTROLLER_REQUEST_MAPPING, ControllerConstants.CATEGORY_CREATE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.POST, CREATE_CATEGORY_URI).content(createCategoryRequest);

        enableTokenAuthentication();

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, HttpStatus.valueOf(response.getStatus()));

        ExpenseTrackerError errorResponse = CommonUtils.parse(response.getContentAsString(), ExpenseTrackerError.class);
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(ExpenseError.CATEGORY_ALREADY_EXISTS, errorResponse.getErrorCode());
        Assertions.assertEquals(ErrorType.BAD_REQUEST, errorResponse.getErrorType());
        Assertions.assertEquals(ExpenseError.CATEGORY_ALREADY_EXISTS.getDescription(), errorResponse.getErrorDescription());
    }

    @Test
    @DisplayName("Change Category")
    @Order(3)
    public void changeCategoryTest() throws Exception {
        MockedCategoryDetails mockedCategoryDetails = mockedCategoryDetailsMap.get(IntegrationTestConstants.CREATED_TEST_CATEGORY);
        String categoryId = mockedCategoryDetails.getCategoryId();

        String changeCategoryRequest = CategoryRequestAgent.fetchChangeCategoryRequest(categoryId);

        final String CHANGE_CATEGORY_URI = String.format("%s%s", ControllerConstants.CATEGORY_CONTROLLER_REQUEST_MAPPING, ControllerConstants.CATEGORY_CHANGE_MAPPING);
        MockHttpServletRequestBuilder request = CommonUtils.fetchMvcRequest(HttpMethod.PUT, CHANGE_CATEGORY_URI).content(changeCategoryRequest);

        enableTokenAuthentication();

        MvcResult mvcResult = executeAndFetchMvcResult(request);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, HttpStatus.valueOf(response.getStatus()));

        CategoryDTO categoryResponse = CommonUtils.parse(response.getContentAsString(), CategoryDTO.class);
        Assertions.assertNotNull(categoryResponse.getCategoryId());

        Category category = categoryRepository.findById(categoryResponse.getCategoryId()).orElseThrow();
        Assertions.assertEquals(category.getCategoryId(), categoryResponse.getCategoryId());
        Assertions.assertEquals(category.getGroup(), categoryResponse.getGroup());
        Assertions.assertEquals(category.getTitle(), categoryResponse.getTitle());

        // put the details in map for other tests
        mockedCategoryDetails = CommonUtils.buildMockedCategoryDetails(category);
        mockedCategoryDetailsMap.put(IntegrationTestConstants.CREATED_TEST_CATEGORY, mockedCategoryDetails);
    }

    @Test
    @DisplayName("Change Category - Category Already Exists")
    @Order(4)
    public void changeCategoryAlreadyExistsTest() throws Exception {
        String createCategoryRequest = CategoryRequestAgent.fetchCreateCategoryRequest();

        final String CREATE_CATEGORY_URI = String.format("%s%s", ControllerConstants.CATEGORY_CONTROLLER_REQUEST_MAPPING, ControllerConstants.CATEGORY_CREATE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.POST, CREATE_CATEGORY_URI).content(createCategoryRequest);

        enableTokenAuthentication();

        executeAndFetchMvcResult(mvcRequest);

        MockedCategoryDetails mockedCategoryDetails = mockedCategoryDetailsMap.get(IntegrationTestConstants.CREATED_TEST_CATEGORY);
        String categoryId = mockedCategoryDetails.getCategoryId();

        String changeCategoryRequest = CategoryRequestAgent.fetchChangeCategoryRequest(categoryId)
                .replace(CHANGED_TITLE, TEST_TITLE);

        final String CHANGE_CATEGORY_URI = String.format("%s%s", ControllerConstants.CATEGORY_CONTROLLER_REQUEST_MAPPING, ControllerConstants.CATEGORY_CHANGE_MAPPING);
        MockHttpServletRequestBuilder request = CommonUtils.fetchMvcRequest(HttpMethod.PUT, CHANGE_CATEGORY_URI).content(changeCategoryRequest);

        MvcResult mvcResult = executeAndFetchMvcResult(request);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, HttpStatus.valueOf(response.getStatus()));

        ExpenseTrackerError errorResponse = CommonUtils.parse(response.getContentAsString(), ExpenseTrackerError.class);
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(ExpenseError.CATEGORY_ALREADY_EXISTS, errorResponse.getErrorCode());
        Assertions.assertEquals(ErrorType.BAD_REQUEST, errorResponse.getErrorType());
        Assertions.assertEquals(ErrorConstants.CATEGORY_ALREADY_EXISTS_MESSAGE, errorResponse.getErrorDescription());
    }

    @Test
    @DisplayName("Change Category - Invalid Auth Response (No username)")
    @Order(3)
    public void changeCategoryInvalidAuthResponseTest() throws Exception {
        MockedCategoryDetails mockedCategoryDetails = mockedCategoryDetailsMap.get(IntegrationTestConstants.CREATED_TEST_CATEGORY);
        String categoryId = mockedCategoryDetails.getCategoryId();

        String changeCategoryRequest = CategoryRequestAgent.fetchChangeCategoryFailureRequest(categoryId);

        final String CHANGE_CATEGORY_URI = String.format("%s%s", ControllerConstants.CATEGORY_CONTROLLER_REQUEST_MAPPING, ControllerConstants.CATEGORY_CHANGE_MAPPING);
        MockHttpServletRequestBuilder request = CommonUtils.fetchMvcRequest(HttpMethod.PUT, CHANGE_CATEGORY_URI).content(changeCategoryRequest);

        MvcResult mvcResult = executeAndFetchMvcResult(request);
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
    @DisplayName("Change Category - Category Does Not Exists")
    @Order(3)
    public void changeCategoryDoesNotExistsTest() throws Exception {
        String categoryId = CommonUtils.generateUUID();

        String changeCategoryRequest = CategoryRequestAgent.fetchChangeCategoryRequest(categoryId);

        final String CHANGE_CATEGORY_URI = String.format("%s%s", ControllerConstants.CATEGORY_CONTROLLER_REQUEST_MAPPING, ControllerConstants.CATEGORY_CHANGE_MAPPING);
        MockHttpServletRequestBuilder request = CommonUtils.fetchMvcRequest(HttpMethod.PUT, CHANGE_CATEGORY_URI).content(changeCategoryRequest);

        enableTokenAuthentication();

        MvcResult mvcResult = executeAndFetchMvcResult(request);
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
    @DisplayName("Change Category - Validation Failure")
    @Order(3)
    public void changeCategoryValidationFailureTest() throws Exception {
        MockedCategoryDetails mockedCategoryDetails = mockedCategoryDetailsMap.get(IntegrationTestConstants.CREATED_TEST_CATEGORY);
        String categoryId = mockedCategoryDetails.getCategoryId();

        String changeCategoryRequest = CategoryRequestAgent.fetchChangeCategoryFailureRequest(categoryId);

        final String CHANGE_CATEGORY_URI = String.format("%s%s", ControllerConstants.CATEGORY_CONTROLLER_REQUEST_MAPPING, ControllerConstants.CATEGORY_CHANGE_MAPPING);
        MockHttpServletRequestBuilder request = CommonUtils.fetchMvcRequest(HttpMethod.PUT, CHANGE_CATEGORY_URI).content(changeCategoryRequest);

        enableTokenAuthentication();

        MvcResult mvcResult = executeAndFetchMvcResult(request);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, HttpStatus.valueOf(response.getStatus()));

        ExpenseTrackerError errorResponse = CommonUtils.parse(response.getContentAsString(), ExpenseTrackerError.class);
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(ExpenseError.BAD_REQUEST, errorResponse.getErrorCode());
        Assertions.assertEquals(ErrorType.BAD_REQUEST, errorResponse.getErrorType());
        Assertions.assertEquals(ExpenseError.BAD_REQUEST.getDescription(), errorResponse.getErrorDescription());
    }

    @Test
    @Order(4)
    @DisplayName("Retrieve Category - Invalid Auth Response (No username)")
    public void retrieveCategoryInvalidAuthResponseTest() throws Exception {
        MockedCategoryDetails mockedCategoryDetails = mockedCategoryDetailsMap.get(IntegrationTestConstants.CREATED_TEST_CATEGORY);
        String categoryId = mockedCategoryDetails.getCategoryId();

        final String RETRIEVE_CATEGORY_URI = String.format("%s%s", ControllerConstants.CATEGORY_CONTROLLER_REQUEST_MAPPING, ControllerConstants.CATEGORY_RETRIEVE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.GET, RETRIEVE_CATEGORY_URI, categoryId);

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
    @DisplayName("Retrieve Category - Category Does Not Exists")
    public void retrieveCategoryDoesNotExistsTest() throws Exception {
        String categoryId = CommonUtils.generateUUID();

        final String RETRIEVE_CATEGORY_URI = String.format("%s%s", ControllerConstants.CATEGORY_CONTROLLER_REQUEST_MAPPING, ControllerConstants.CATEGORY_RETRIEVE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.GET, RETRIEVE_CATEGORY_URI, categoryId);

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
    @DisplayName("Retrieve Category")
    public void retrieveCategoryTest() throws Exception {
        MockedCategoryDetails mockedCategoryDetails = mockedCategoryDetailsMap.get(IntegrationTestConstants.CREATED_TEST_CATEGORY);
        String categoryId = mockedCategoryDetails.getCategoryId();

        final String RETRIEVE_CATEGORY_URI = String.format("%s%s", ControllerConstants.CATEGORY_CONTROLLER_REQUEST_MAPPING, ControllerConstants.CATEGORY_RETRIEVE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.GET, RETRIEVE_CATEGORY_URI, categoryId);

        enableTokenAuthentication();

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, HttpStatus.valueOf(response.getStatus()));

        CategoryDTO categoryResponse = CommonUtils.parse(response.getContentAsString(), CategoryDTO.class);
        Assertions.assertNotNull(categoryResponse.getCategoryId());

        Category category = categoryRepository.findById(categoryResponse.getCategoryId()).orElseThrow();
        Assertions.assertEquals(category.getCategoryId(), categoryResponse.getCategoryId());
        Assertions.assertEquals(category.getGroup(), categoryResponse.getGroup());
        Assertions.assertEquals(category.getTitle(), categoryResponse.getTitle());
    }

    @Test
    @Order(4)
    @DisplayName("Retrieve Categories")
    public void retrieveCategoriesTest() throws Exception {
        final String RETRIEVE_ALL_CATEGORIES_URI = String.format("%s%s", ControllerConstants.CATEGORY_CONTROLLER_REQUEST_MAPPING, ControllerConstants.CATEGORY_RETRIEVE_ALL_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.GET, RETRIEVE_ALL_CATEGORIES_URI);

        enableTokenAuthentication();

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, HttpStatus.valueOf(response.getStatus()));

        List<CategoryDTO> categoryResponse = CommonUtils.parse(response.getContentAsString(), new TypeReference<>() {
        });
        Assertions.assertNotNull(categoryResponse);

        categoryResponse.forEach(categoryDTO -> {
            Category category = categoryRepository.findById(categoryDTO.getCategoryId()).orElseThrow();
            Assertions.assertEquals(category.getCategoryId(), categoryDTO.getCategoryId());
            Assertions.assertEquals(category.getGroup(), categoryDTO.getGroup());
            Assertions.assertEquals(category.getTitle(), categoryDTO.getTitle());
        });
    }

    @Test
    @Order(4)
    @DisplayName("Retrieve Categories - Invalid Auth Response (No username)")
    public void retrieveCategoriesInvalidAuthResponseTest() throws Exception {
        final String RETRIEVE_ALL_CATEGORIES_URI = String.format("%s%s", ControllerConstants.CATEGORY_CONTROLLER_REQUEST_MAPPING, ControllerConstants.CATEGORY_RETRIEVE_ALL_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.GET, RETRIEVE_ALL_CATEGORIES_URI);

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
    @DisplayName("Delete Category")
    public void deleteCategoryTest() throws Exception {
        MockedCategoryDetails mockedCategoryDetails = mockedCategoryDetailsMap.get(IntegrationTestConstants.CREATED_TEST_CATEGORY);
        String categoryId = mockedCategoryDetails.getCategoryId();

        final String DELETE_CATEGORY_URI = String.format("%s%s", ControllerConstants.CATEGORY_CONTROLLER_REQUEST_MAPPING, ControllerConstants.CATEGORY_DELETE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.DELETE, DELETE_CATEGORY_URI, categoryId);

        enableTokenAuthentication();

        MvcResult mvcResult = executeAndFetchMvcResult(mvcRequest);
        Assertions.assertNotNull(mvcResult);

        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.ACCEPTED, HttpStatus.valueOf(response.getStatus()));

        Category category = categoryRepository.findById(categoryId).orElse(null);
        Assertions.assertNull(category);
    }

    @Test
    @DisplayName("Delete Category - Invalid Auth Response (No username)")
    public void deleteCategoryInvalidAuthResponseTest() throws Exception {
        MockedCategoryDetails mockedCategoryDetails = mockedCategoryDetailsMap.get(IntegrationTestConstants.CREATED_TEST_CATEGORY);
        String categoryId = mockedCategoryDetails.getCategoryId();

        final String DELETE_CATEGORY_URI = String.format("%s%s", ControllerConstants.CATEGORY_CONTROLLER_REQUEST_MAPPING, ControllerConstants.CATEGORY_DELETE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.DELETE, DELETE_CATEGORY_URI, categoryId);

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
    @DisplayName("Delete Category - Category Does Not Exists")
    public void deleteCategoryDoesNotExistsTest() throws Exception {
        String categoryId = CommonUtils.generateUUID();

        final String DELETE_CATEGORY_URI = String.format("%s%s", ControllerConstants.CATEGORY_CONTROLLER_REQUEST_MAPPING, ControllerConstants.CATEGORY_DELETE_MAPPING);
        MockHttpServletRequestBuilder mvcRequest = CommonUtils.fetchMvcRequest(HttpMethod.DELETE, DELETE_CATEGORY_URI, categoryId);

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
}
