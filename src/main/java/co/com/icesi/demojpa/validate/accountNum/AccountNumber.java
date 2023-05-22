package co.com.icesi.demojpa.validate.accountNum;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Constraint(validatedBy = AccountNumberValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccountNumber {
    String message() default "El numero de cuenta no es valido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
