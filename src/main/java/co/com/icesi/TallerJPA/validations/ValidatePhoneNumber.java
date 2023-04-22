package co.com.icesi.TallerJPA.validations;

import com.nimbusds.jose.Payload;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UserPhoneValidator.class)
public @interface ValidatePhoneNumber {
 public String message() default "Invalid phone number: It should be a colombian phone number format";

 Class<?>[] groups() default {};

 Class<? extends Payload>[] payload() default {};
}
