package com.clickbait.plugin.annotations;

import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SqlExceptionPass {
    String message() default "{SqlExceptionPass}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}