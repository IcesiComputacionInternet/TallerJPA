package co.com.icesi.TallerJPA.validations;

import com.nimbusds.jose.Payload;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UserPhoneAndEmailValidator.class)
public @interface ValidatePhoneNumberAndEmail {
    public String message() default "Invalid phone number and email";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
