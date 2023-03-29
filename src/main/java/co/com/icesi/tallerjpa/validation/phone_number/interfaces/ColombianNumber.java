package co.com.icesi.tallerjpa.validation.phone_number.interfaces;

import co.com.icesi.tallerjpa.validation.phone_number.ColombianNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = ColombianNumberValidator.class)
@Target( {ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE} )
@Retention(RetentionPolicy.RUNTIME)
public @interface ColombianNumber {
    String message() default "Phone number is not colombian";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
