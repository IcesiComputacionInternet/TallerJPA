package co.edu.icesi.demo.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ContactNumberValidator.class)
@Target( { ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailOrPhoneConstraint {
    String message() default "At least fill the email or phone";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
