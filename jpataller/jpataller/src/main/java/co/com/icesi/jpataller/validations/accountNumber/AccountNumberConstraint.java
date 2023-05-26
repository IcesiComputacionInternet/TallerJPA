package co.com.icesi.jpataller.validations.accountNumber;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Constraint(validatedBy = AccountNumberValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccountNumberConstraint {
    String message() default "El número de cuenta no es valido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
