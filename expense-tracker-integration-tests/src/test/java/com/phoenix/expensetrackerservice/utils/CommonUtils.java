package com.phoenix.expensetrackerservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.expensetrackerservice.constants.AuthConstants;
import com.phoenix.expensetrackerservice.constants.IntegrationTestConstants;
import com.phoenix.expensetrackerservice.entity.Category;
import com.phoenix.expensetrackerservice.entity.Transaction;
import com.phoenix.expensetrackerservice.model.MockedCategoryDetails;
import com.phoenix.expensetrackerservice.model.MockedTransactionDetails;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

@UtilityClass
public class CommonUtils {

    private static final String AUTH_PRINCIPAL = "{ \"name\": \"%s\"}".formatted(IntegrationTestConstants.USERNAME);
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    public static String readJson(String path) throws IOException {
        return FileUtils.readFileToString(new File(path), "UTF-8");
    }

    public MockHttpServletRequestBuilder fetchMvcRequest(HttpMethod method, String uri, Object... uriVariables) {
        return MockMvcRequestBuilders.request(method, uri, uriVariables).contentType(MediaType.APPLICATION_JSON)
                .header(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_PREFIX);
    }

    public static MockedCategoryDetails buildMockedCategoryDetails(Category category) {
        return MockedCategoryDetails.builder()
                .categoryId(category.getCategoryId())
                .title(category.getTitle())
                .username(category.getUsername())
                .build();
    }

    public static MockedTransactionDetails buildMockedTransactionDetails(Transaction transaction) {
        return MockedTransactionDetails.builder()
                .transactionId(transaction.getTransactionId())
                .build();
    }

    public static String todaysDate() {
        ZonedDateTime date = ZonedDateTime.now();
        return String.format("%s-%s-%s", date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }

    public static <T> T parse(String content, Class<? extends T> expectedType) throws JsonProcessingException {
        return objectMapper.readValue(content, expectedType);
    }

    public static <T> T parse(String content, TypeReference<? extends T> expectedType) throws JsonProcessingException {
        return objectMapper.readValue(content, expectedType);
    }

    public static <T> T fetchContent(String path, Class<? extends T> expectedType) throws IOException {
        return parse(readJson(path), expectedType);
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
