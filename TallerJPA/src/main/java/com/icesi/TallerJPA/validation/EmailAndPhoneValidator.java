package com.icesi.TallerJPA.validation;

import com.icesi.TallerJPA.validation.notation.ValidateExistEmailAndPhone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailAndPhoneValidator implements ConstraintValidator<ValidateExistEmailAndPhone, String> {

    @Override
    public void initialize(ValidateExistEmailAndPhone constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
