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
    public Object sqlExceptionPass(ProceedingJoinPoint pjp) throws Throwable {
        Object proceed = null;

        try {
            proceed = pjp.proceed();
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

        return proceed;
    }
}