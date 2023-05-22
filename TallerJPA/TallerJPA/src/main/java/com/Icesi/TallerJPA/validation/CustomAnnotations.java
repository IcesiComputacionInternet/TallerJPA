package com.Icesi.TallerJPA.validation;

import java.lang.annotation.*;
import javax.validation.Payload;
import javax.validation.Constraint;
public interface CustomAnnotations {

    @Documented
    @Constraint(validatedBy = PasswordValidator.class)
    @Target({ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface PasswordValidation {
        String message() default "Name is invalid";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }
}