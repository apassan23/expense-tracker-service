package com.phoenix.expensetrackerservice.strategy.utils.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

class DateParserStrategyImplTest {

    @Test
    void withDefaultPatternTest() {
        // prepare
        final String DATE = "1999-02-23";
        DateParserStrategyImpl dateParserStrategy = DateParserStrategyImpl.withDefaultPattern();

        // actions & assert
        Date date = Assertions.assertDoesNotThrow(() -> dateParserStrategy.parse(DATE));
        Assertions.assertNotNull(date);
    }

    @Test
    void withPatternTest() {
        // prepare
        final String DATE = "23-02-1999";
        final String PATTERN = "dd-MM-yyyy";
        DateParserStrategyImpl dateParserStrategy = DateParserStrategyImpl.withPattern(PATTERN);

        // actions & assert
        Date date = Assertions.assertDoesNotThrow(() -> dateParserStrategy.parse(DATE));
        Assertions.assertNotNull(date);
    }

    @Test
    void withDefaultPatternWithWrongPatternTest() {
        // prepare
        final String DATE = "23-02-1999";
        DateParserStrategyImpl dateParserStrategy = DateParserStrategyImpl.withDefaultPattern();

        // actions & assert
        Assertions.assertThrows(Exception.class, () -> dateParserStrategy.parse(DATE));
    }
}