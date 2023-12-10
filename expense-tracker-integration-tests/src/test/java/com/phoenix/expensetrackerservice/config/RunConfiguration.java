package com.phoenix.expensetrackerservice.config;

import com.fasterxml.jackson.databind.node.POJONode;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.phoenix.expensetrackerservice.ExpenseTrackerServiceApplication;
import com.phoenix.expensetrackerservice.config.mongo.CategoryRepositoryIT;
import com.phoenix.expensetrackerservice.config.mongo.TransactionRepositoryIT;
import com.phoenix.expensetrackerservice.constants.IntegrationTestConstants;
import com.phoenix.expensetrackerservice.constants.WiremockConstants;
import com.phoenix.expensetrackerservice.model.AuthServiceResponse;
import com.phoenix.expensetrackerservice.model.MockedCategoryDetails;
import com.phoenix.expensetrackerservice.model.MockedTransactionDetails;
import com.phoenix.expensetrackerservice.utils.CommonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.badRequest;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest(classes = ExpenseTrackerServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class RunConfiguration {

    private static WireMockServer wireMockServer;
    protected static Map<String, MockedCategoryDetails> mockedCategoryDetailsMap = new ConcurrentHashMap<>();
    protected static Map<String, MockedTransactionDetails> mockedTransactionDetailsMap = new ConcurrentHashMap<>();

    @Autowired
    protected TransactionRepositoryIT transactionRepository;
    @Autowired
    protected CategoryRepositoryIT categoryRepository;
    @Autowired
    protected MockMvc mockMvc;


    static {
        startWiremockServer();
    }

    @BeforeEach
    void setup() {
        stubFor(post(urlEqualTo(WiremockConstants.AUTH_SERVICE_URI))
                .willReturn(badRequest()));
    }

    protected void enableTokenAuthentication() throws IOException {
        stubFor(post(urlEqualTo(WiremockConstants.AUTH_SERVICE_URI))
                .willReturn(aResponse().withHeader(WiremockConstants.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).withJsonBody(new POJONode(CommonUtils.fetchContent(IntegrationTestConstants.AUTH_SERVICE_RESPONSE_PATH, AuthServiceResponse.class)))));
    }

    private static void startWiremockServer() {
        wireMockServer = new WireMockServer(WiremockConstants.PORT);
        wireMockServer.start();
        if (Boolean.FALSE.equals(wireMockServer.isRunning())) {
            throw new RuntimeException();
        }
    }

    private static void stopWiremockServer() {
        wireMockServer.stop();
    }

    protected MvcResult executeAndFetchMvcResult(RequestBuilder request) throws Exception {
        return mockMvc.perform(request).andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }
}
