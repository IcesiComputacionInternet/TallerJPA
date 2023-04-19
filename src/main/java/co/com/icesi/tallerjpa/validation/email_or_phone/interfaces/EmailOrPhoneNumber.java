package co.com.icesi.tallerjpa.validation.email_or_phone.interfaces;

import co.com.icesi.tallerjpa.validation.email_or_phone.EmailOrPhoneNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EmailOrPhoneNumberValidator.class)
@Target( {ElementType.TYPE, ElementType.ANNOTATION_TYPE} )
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailOrPhoneNumber {
    String message() default "the email or the phoneNumber must have a value";
    String field() default "email or phoneNumber";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
