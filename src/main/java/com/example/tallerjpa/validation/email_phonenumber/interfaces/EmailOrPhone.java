package com.example.tallerjpa.validation.email_phonenumber.interfaces;

import com.example.tallerjpa.validation.email_phonenumber.EmailOrPhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EmailOrPhoneValidator.class)
@Target( {ElementType.TYPE, ElementType.ANNOTATION_TYPE} )
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailOrPhone{
    String message() default "The email or the phoneNumber must have a value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}