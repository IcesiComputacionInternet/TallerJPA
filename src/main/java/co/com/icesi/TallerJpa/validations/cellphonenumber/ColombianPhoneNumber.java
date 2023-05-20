package co.com.icesi.TallerJpa.validations.cellphonenumber;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ColombianPhoneNumberValidator.class)
public @interface ColombianPhoneNumber {
    String message() default "Numero Invalido, no es de Colombia.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


