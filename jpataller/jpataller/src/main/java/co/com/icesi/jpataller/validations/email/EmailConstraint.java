package co.com.icesi.jpataller.validations.email;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailConstraint {
    String message() default "El correo no es valido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
