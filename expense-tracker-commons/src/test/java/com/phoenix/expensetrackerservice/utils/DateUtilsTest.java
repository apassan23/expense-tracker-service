package com.phoenix.expensetrackerservice.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

class DateUtilsTest {

    @Test
    void parseTest() {
        // prepare
        final String DATE = "1999-02-23";

        // action & assert
        Date date = Assertions.assertDoesNotThrow(() -> DateUtils.parse(DATE));
        Assertions.assertNotNull(date);
    }

    @Test
    void parseInvalidDateTest() {
        // prepare
        final String DATE = "23-02-1999";

        // action & assert
        Assertions.assertThrows(Exception.class, () -> DateUtils.parse(DATE));
    }

    @Test
    void nextDayTest() {
        // prepare
        final Date date = new Date();

        // action & assert
        Date dateWithNextDay = Assertions.assertDoesNotThrow(() -> DateUtils.nextDay(date));
        Assertions.assertNotNull(date);
        Assertions.assertTrue(dateWithNextDay.after(date));
    }
}