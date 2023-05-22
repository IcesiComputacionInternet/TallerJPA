package co.edu.icesi.tallerjpa.runableartefact.validations.phoneValidation.interfaces;

import co.edu.icesi.tallerjpa.runableartefact.validations.phoneValidation.implementation.ColombianNumberValidator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ColombianNumberValidator.class)
public @interface ColombianNumber {
    String message() default "must be a colombian number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
