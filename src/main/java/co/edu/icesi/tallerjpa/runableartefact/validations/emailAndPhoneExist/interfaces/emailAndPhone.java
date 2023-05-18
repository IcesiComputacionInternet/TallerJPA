package co.edu.icesi.tallerjpa.runableartefact.validations.emailAndPhoneExist.interfaces;

import co.edu.icesi.tallerjpa.runableartefact.validations.emailAndPhoneExist.implementation.emailAndPhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = emailAndPhoneValidator.class)
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface emailAndPhone {
    String message() default "the email or the phoneNumber must have a value";
    String field() default "email or phoneNumber";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
