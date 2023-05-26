package co.com.icesi.jpataller.validations.phone;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneConstraint {
    String message() default  "El número de celular no proviene de Colombia";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
