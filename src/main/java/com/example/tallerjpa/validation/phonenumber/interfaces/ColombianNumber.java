package com.example.tallerjpa.validation.phonenumber.interfaces;

import com.example.tallerjpa.validation.phonenumber.ColombianNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ColombianNumberValidator.class)
@Target( {ElementType.FIELD} )

public @interface ColombianNumber {
    String message() default "The phone number must be from Colombia";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}