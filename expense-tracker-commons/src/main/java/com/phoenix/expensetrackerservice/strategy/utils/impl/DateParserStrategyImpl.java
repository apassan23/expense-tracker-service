package com.phoenix.expensetrackerservice.strategy.utils.impl;

import com.phoenix.expensetrackerservice.strategy.utils.DateParserStrategy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.format.datetime.DateFormatter;

import java.util.Date;
import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateParserStrategyImpl implements DateParserStrategy {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private DateFormatter dateFormatter;

    public static DateParserStrategyImpl withDefaultPattern() {
        DateParserStrategyImpl dateParserStrategy = new DateParserStrategyImpl();
        dateParserStrategy.dateFormatter = new DateFormatter(DATE_FORMAT);
        return dateParserStrategy;
    }

    public static DateParserStrategyImpl withPattern(String pattern) {
        DateParserStrategyImpl dateParserStrategy = new DateParserStrategyImpl();
        dateParserStrategy.dateFormatter = new DateFormatter(pattern);
        return dateParserStrategy;
    }


    @Override
    public Date parse(String date) throws Exception {
        return dateFormatter.parse(date, Locale.getDefault());
    }
}
