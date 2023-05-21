package com.edu.icesi.TallerJPA.Constraint;

import com.edu.icesi.TallerJPA.validator.PhoneAndEmailValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = PhoneAndEmailValidation.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneAndEmailConstraint {

    String message() default "User need a email and phone number";

    String email();

    String phoneNumber();

    Class<?>[] groups() default{};

    Class<? extends Payload[]>[] payload() default{};
}
