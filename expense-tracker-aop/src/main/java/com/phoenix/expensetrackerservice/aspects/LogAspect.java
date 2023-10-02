package com.phoenix.expensetrackerservice.aspects;

import com.phoenix.expensetrackerservice.annotation.Log;
import com.phoenix.expensetrackerservice.constants.MDCKeys;
import com.phoenix.expensetrackerservice.models.Payload;
import com.phoenix.expensetrackerservice.transform.PayloadBuilder;
import com.phoenix.expensetrackerservice.utils.JsonUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.event.Level;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
@Aspect
public class LogAspect {
    private final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Around("@annotation(com.phoenix.expensetrackerservice.annotation.Log)")
    public Object logPayload(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Log logAnnotation = getAnnotation(proceedingJoinPoint.getSignature());

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object[] args = proceedingJoinPoint.getArgs();
        Object response = proceedingJoinPoint.proceed(args);
        stopWatch.stop();

        try {
            Payload payload;
            if(response instanceof ResponseEntity<?> responseEntity) {
                payload = PayloadBuilder.build(logAnnotation.action(), args, stopWatch.getLastTaskTimeMillis(), responseEntity.getBody());
            } else {
                payload = PayloadBuilder.build(logAnnotation.action(), args, stopWatch.getLastTaskTimeMillis(), response);
            }
            Converter<Payload, ? extends Serializable> converter = getConverter(logAnnotation);
            Serializable convertedPayload = converter.convert(payload);
            setMDC(payload);
            Level loggerLevel = logAnnotation.level();
            LOGGER.atLevel(loggerLevel).log("{}: {}", payload.getAction(), JsonUtils.getJsonString(convertedPayload));
        } catch (Exception e) {
            LOGGER.error("Error occurred while logging the payload", e);
        }

        return response;
    }

    @AfterThrowing(value = "@annotation(com.phoenix.expensetrackerservice.annotation.Log)", throwing = "throwable")
    public void handleException(Throwable throwable) throws Throwable {
        LOGGER.error("Error Occurred", throwable);
        throw throwable;
    }

    private Converter<Payload, ? extends Serializable> getConverter(Log log) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return log.converter().getDeclaredConstructor().newInstance();
    }

    private Log getAnnotation(Signature signature) {
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        return method.getDeclaredAnnotation(Log.class);
    }

    private void setMDC(Payload payload) {
        MDC.put(MDCKeys.ACTION, payload.getAction());
        MDC.put(MDCKeys.EXECUTION_TIME, String.valueOf(payload.getExecutionTime()));
    }
}
