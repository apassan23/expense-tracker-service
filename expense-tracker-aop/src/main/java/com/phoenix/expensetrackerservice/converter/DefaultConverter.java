package com.phoenix.expensetrackerservice.converter;

import com.phoenix.expensetrackerservice.models.Payload;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

public class DefaultConverter implements Converter<Payload, Payload> {
    @Override
    public Payload convert(@NonNull Payload source) {
        return source;
    }
}
