package com.clickbait.plugin.annotations;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SqlExceptionPassAspect {

    @Around("@annotation(SqlExceptionPass)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = null;

        try {
            proceed = joinPoint.proceed();
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

        return proceed;
    }
}