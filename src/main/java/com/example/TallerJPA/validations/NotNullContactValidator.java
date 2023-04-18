package com.example.TallerJPA.validations;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
public class NotNullContactValidator implements ConstraintValidator<NotNullContact, Object> {

    private String phone;
    private String email;

    public void initialize(NotNullContact constraintAnnotation) {
        this.phone = constraintAnnotation.phone();
        this.email = constraintAnnotation.email();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object phoneValue = new BeanWrapperImpl(value)
                .getPropertyValue(phone);
        Object fieldMatchValue = new BeanWrapperImpl(value)
                .getPropertyValue(email);
        return phoneValue != null && email != null;
    }
}