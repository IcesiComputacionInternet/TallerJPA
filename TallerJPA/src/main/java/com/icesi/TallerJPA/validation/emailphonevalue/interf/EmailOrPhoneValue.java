package com.icesi.TallerJPA.validation.emailphonevalue.interf;

import com.icesi.TallerJPA.validation.emailphonevalue.EmailOrPhoneValueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EmailOrPhoneValueValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailOrPhoneValue {
    String message() default "Email or phone must have a value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}