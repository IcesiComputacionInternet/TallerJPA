package com.example.jpa.validations.interfaces;

import com.example.jpa.validations.implementations.RegionPhoneNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = RegionPhoneNumberValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RegionPhoneNumberValidation {
    String message() default "Invalid input";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
