package co.edu.icesi.demo.validation.constraint;

import co.edu.icesi.demo.validation.validator.EmailOrPhoneNumberExistValidator;
import co.edu.icesi.demo.validation.validator.TransactionAccountValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TransactionAccountValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface TransactionAccountConstraint {
    String message() default "An account number is needed";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
