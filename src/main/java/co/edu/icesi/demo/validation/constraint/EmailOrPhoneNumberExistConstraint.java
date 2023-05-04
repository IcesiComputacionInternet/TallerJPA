package co.edu.icesi.demo.validation.constraint;


import co.edu.icesi.demo.validation.validator.EmailOrPhoneNumberExistValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EmailOrPhoneNumberExistValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailOrPhoneNumberExistConstraint {

    String message() default "Email or phone number are needed!";


}
//https://www.baeldung.com/spring-mvc-custom-validator