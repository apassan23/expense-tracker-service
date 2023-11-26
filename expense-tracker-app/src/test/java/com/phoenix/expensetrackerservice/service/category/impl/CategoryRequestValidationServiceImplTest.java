package com.phoenix.expensetrackerservice.service.category.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.expensetrackerservice.exception.ExpenseTrackerBadRequestException;
import com.phoenix.expensetrackerservice.exception.enums.ExpenseError;
import com.phoenix.expensetrackerservice.model.CategoryDTO;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class CategoryRequestValidationServiceImplTest {

    private CategoryRequestValidationServiceImpl categoryRequestValidationService;

    @BeforeEach
    void setup() {
        categoryRequestValidationService = spy(new CategoryRequestValidationServiceImpl());
    }

    @ParameterizedTest
    @NullSource
    @MethodSource(value = "categorySourceForCreate")
    void validateForCreateTest(CategoryDTO categoryDTO) {
        // Action & assert
        ExpenseTrackerBadRequestException exception = Assertions.assertThrows(ExpenseTrackerBadRequestException.class, () -> categoryRequestValidationService.validateForCreate(categoryDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertNotNull(exception.getExpenseError());
        Assertions.assertEquals(ExpenseError.BAD_REQUEST.getDescription(), exception.getMessage());
        Assertions.assertEquals(ExpenseError.BAD_REQUEST, exception.getExpenseError());
    }

    @ParameterizedTest
    @MethodSource(value = "categorySourceHappyPathForCreate")
    void validateForCreateHappyPathTest(CategoryDTO categoryDTO) {
        // Action & assert
        Assertions.assertDoesNotThrow(() -> categoryRequestValidationService.validateForCreate(categoryDTO));
    }

    @ParameterizedTest
    @NullSource
    @MethodSource(value = "categorySourceForChange")
    void validateForChangeTest(CategoryDTO categoryDTO) {
        // Action & assert
        ExpenseTrackerBadRequestException exception = Assertions.assertThrows(ExpenseTrackerBadRequestException.class, () -> categoryRequestValidationService.validateForChange(categoryDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertNotNull(exception.getExpenseError());
        Assertions.assertEquals(ExpenseError.BAD_REQUEST.getDescription(), exception.getMessage());
        Assertions.assertEquals(ExpenseError.BAD_REQUEST, exception.getExpenseError());
    }

    @ParameterizedTest
    @MethodSource(value = "categorySourceHappyPathForChange")
    void validateForChangeHappyPathTest(CategoryDTO categoryDTO) {
        // Action & assert
        Assertions.assertDoesNotThrow(() -> categoryRequestValidationService.validateForChange(categoryDTO));
    }

    @ParameterizedTest
    @NullSource
    @MethodSource(value = "categorySourceForRetrieveAndDelete")
    void validateForRetrieveTest(CategoryDTO categoryDTO) {
        // Action & assert
        ExpenseTrackerBadRequestException exception = Assertions.assertThrows(ExpenseTrackerBadRequestException.class, () -> categoryRequestValidationService.validateForRetrieve(categoryDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertNotNull(exception.getExpenseError());
        Assertions.assertEquals(ExpenseError.BAD_REQUEST.getDescription(), exception.getMessage());
        Assertions.assertEquals(ExpenseError.BAD_REQUEST, exception.getExpenseError());
    }

    @ParameterizedTest
    @MethodSource(value = "categorySourceHappyPathForRetrieveAndDelete")
    void validateForRetrieveHappyPathTest(CategoryDTO categoryDTO) {
        // Action & assert
        Assertions.assertDoesNotThrow(() -> categoryRequestValidationService.validateForRetrieve(categoryDTO));
    }

    @ParameterizedTest
    @NullSource
    @MethodSource(value = "categorySourceForRetrieveAndDelete")
    void validateForDeleteTest(CategoryDTO categoryDTO) {
        // Action & assert
        ExpenseTrackerBadRequestException exception = Assertions.assertThrows(ExpenseTrackerBadRequestException.class, () -> categoryRequestValidationService.validateForDelete(categoryDTO));
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertNotNull(exception.getExpenseError());
        Assertions.assertEquals(ExpenseError.BAD_REQUEST.getDescription(), exception.getMessage());
        Assertions.assertEquals(ExpenseError.BAD_REQUEST, exception.getExpenseError());
    }

    @ParameterizedTest
    @MethodSource(value = "categorySourceHappyPathForRetrieveAndDelete")
    void validateForDeleteHappyPathTest(CategoryDTO categoryDTO) {
        // Action & assert
        Assertions.assertDoesNotThrow(() -> categoryRequestValidationService.validateForDelete(categoryDTO));
    }

    private static Stream<Arguments> categorySourceForRetrieveAndDelete() throws IOException {
        List<CategoryDTO> categoryDTOS = getCategoriesFromFile("src/test/resources/data/Categories.json");

        return categoryDTOS
                .stream()
                .filter(category -> Objects.isNull(category.getCategoryId()))
                .map(Arguments::of);
    }

    private static Stream<Arguments> categorySourceForChange() throws IOException {
        List<CategoryDTO> categoryDTOS = getCategoriesFromFile("src/test/resources/data/Categories.json");

        return categoryDTOS
                .stream()
                .map(Arguments::of);
    }

    private static Stream<Arguments> categorySourceForCreate() throws IOException {
        List<CategoryDTO> categoryDTOS = getCategoriesFromFile("src/test/resources/data/Categories.json");

        return categoryDTOS
                .stream()
                .filter(category -> Objects.isNull(category.getTitle()))
                .map(Arguments::of);
    }

    private static Stream<Arguments> categorySourceHappyPathForCreate() throws IOException {
        List<CategoryDTO> categoryDTOS = getCategoriesFromFile("src/test/resources/data/CategoriesHappyPath.json");

        return categoryDTOS
                .stream()
                .filter(category -> Objects.nonNull(category.getTitle()))
                .map(Arguments::of);
    }

    private static Stream<Arguments> categorySourceHappyPathForChange() throws IOException {
        List<CategoryDTO> categoryDTOS = getCategoriesFromFile("src/test/resources/data/CategoriesHappyPath.json");

        return categoryDTOS
                .stream()
                .filter(category -> Objects.nonNull(category.getTitle()) && Objects.nonNull(category.getCategoryId()) && Objects.nonNull(category.getGroup()) && Objects.nonNull(category.getDescription()))
                .map(Arguments::of);
    }

    private static Stream<Arguments> categorySourceHappyPathForRetrieveAndDelete() throws IOException {
        List<CategoryDTO> categoryDTOS = getCategoriesFromFile("src/test/resources/data/CategoriesHappyPath.json");

        return categoryDTOS
                .stream()
                .filter(category -> Objects.nonNull(category.getCategoryId()))
                .map(Arguments::of);
    }

    private static List<CategoryDTO> getCategoriesFromFile(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        String json = FileUtils.readFileToString(new File(path), "UTF-8");

        return objectMapper.readValue(json, new TypeReference<>() {
        });
    }
}