package co.com.icesi.tallerjpa.constraint;

import co.com.icesi.tallerjpa.validators.EmailorPhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailorPhoneValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailorPhoneConstraint {
    String message() default "Invalid phone number or email";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
