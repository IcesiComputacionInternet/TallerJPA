package com.edu.icesi.TallerJPA.Constraint;

import com.edu.icesi.TallerJPA.validator.ContactNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ContactNumberValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ContactNumberConstraint {
    String message() default "Invalid phone number";

    Class<?>[] groups() default {};

    Class<? extends Payload[]>[] payload() default {};
}
