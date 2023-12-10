package com.phoenix.expensetrackerservice.strategy.utils.impl;

import com.phoenix.expensetrackerservice.strategy.utils.DateParserStrategy;
import org.springframework.format.datetime.DateFormatter;

import java.util.Date;
import java.util.Locale;

public class DateParserStrategyImpl implements DateParserStrategy {
    private final DateFormatter dateFormatter;

    public DateParserStrategyImpl(DateFormatter dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    @Override
    public Date parse(String date) throws Exception {
        return dateFormatter.parse(date, Locale.getDefault());
    }
}
