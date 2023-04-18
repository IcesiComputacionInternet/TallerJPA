package com.icesi.TallerJPA.validation.phonenumber.interf;

import com.icesi.TallerJPA.validation.phonenumber.ColombiaPhoneNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = ColombiaPhoneNumberValidator.class)
@Target( {ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE} )
@Retention(RetentionPolicy.RUNTIME)
public @interface ColombiaPhoneNumber {
    String message() default "Phone number is not colombian";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}