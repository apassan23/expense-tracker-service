package com.phoenix.expensetrackerservice.annotation;

import com.phoenix.expensetrackerservice.converter.DefaultConverter;
import com.phoenix.expensetrackerservice.models.Payload;
import org.slf4j.event.Level;
import org.springframework.core.convert.converter.Converter;

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Log {
    String action();
    Level level() default Level.INFO;
    Class<? extends Converter<Payload, ? extends Serializable>> converter() default DefaultConverter.class;
}
