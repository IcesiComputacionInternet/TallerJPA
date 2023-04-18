package com.example.jpa.validations.interfaces;

import com.example.jpa.validations.implementations.AtLeastOneNotNullValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtLeastOneNotNullValidator.class)
public @interface AtLeastOneNotNull {

    String message() default "Al menos un atributo debe ser no nulo";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] fields();
}
