package co.com.icesi.icesiAccountSystem.validation.interfaces;

import co.com.icesi.icesiAccountSystem.validation.validators.EmailAndPhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailAndPhoneValidator.class)
@Target( { ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailAndPhoneConstraint {
    String message() default "Phone number and email fields must have a value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
