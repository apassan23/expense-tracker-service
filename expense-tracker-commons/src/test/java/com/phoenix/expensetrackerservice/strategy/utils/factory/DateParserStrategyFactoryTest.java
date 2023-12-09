package com.phoenix.expensetrackerservice.strategy.utils.factory;

import com.phoenix.expensetrackerservice.strategy.utils.DateParserStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

class DateParserStrategyFactoryTest {

    @Test
    void withDefaultPatternTest() {
        // prepare
        final String DATE = "1999-02-23";
        DateParserStrategy dateParserStrategy = DateParserStrategyFactory.withDefaultPattern();

        // actions & assert
        Date date = Assertions.assertDoesNotThrow(() -> dateParserStrategy.parse(DATE));
        Assertions.assertNotNull(date);
    }

    @Test
    void withPatternTest() {
        // prepare
        final String DATE = "23-02-1999";
        final String PATTERN = "dd-MM-yyyy";
        DateParserStrategy dateParserStrategy = DateParserStrategyFactory.withPattern(PATTERN);

        // actions & assert
        Date date = Assertions.assertDoesNotThrow(() -> dateParserStrategy.parse(DATE));
        Assertions.assertNotNull(date);
    }

    @Test
    void withDefaultPatternWithWrongPatternTest() {
        // prepare
        final String DATE = "23-02-1999";
        DateParserStrategy dateParserStrategy = DateParserStrategyFactory.withDefaultPattern();

        // actions & assert
        Assertions.assertThrows(Exception.class, () -> dateParserStrategy.parse(DATE));
    }
}