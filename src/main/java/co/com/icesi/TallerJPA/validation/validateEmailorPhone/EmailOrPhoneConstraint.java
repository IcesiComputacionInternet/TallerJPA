package co.com.icesi.TallerJPA.validation.validateEmailorPhone;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailOrPhoneValidator.class)
@Target( { ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailOrPhoneConstraint {
    String message() default "Email or phone must have a value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
