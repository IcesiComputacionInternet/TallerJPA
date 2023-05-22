package com.example.jpa.validations.interfaces;

import com.example.jpa.validations.implementations.AtLeastOneNotNullValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtLeastOneNotNullValidator.class)
public @interface AtLeastOneNotNull {

    String message() default "Email or phone number must have a value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
