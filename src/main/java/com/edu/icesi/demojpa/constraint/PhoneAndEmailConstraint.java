package com.edu.icesi.demojpa.constraint;

import com.edu.icesi.demojpa.validator.PhoneAndNumberValidator;
import com.edu.icesi.demojpa.validator.PhoneNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PhoneAndNumberValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneAndEmailConstraint {
    String message() default "An email and phone number are required";
    Class<?>[] groups() default {};
    Class<? extends Payload[]>[] payload() default {};
}
