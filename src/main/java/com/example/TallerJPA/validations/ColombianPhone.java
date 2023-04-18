package com.example.TallerJPA.validations;

import com.example.TallerJPA.validations.ColombianPhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ColombianPhoneValidator.class)
@Documented
public @interface ColombianPhone {

    String message() default "{com.ricardorb.config.constraints.FullAge}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};



}