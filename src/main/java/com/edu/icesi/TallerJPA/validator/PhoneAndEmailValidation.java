package com.edu.icesi.TallerJPA.validator;

import com.edu.icesi.TallerJPA.Constraint.PhoneAndEmailConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneAndEmailValidation implements ConstraintValidator<PhoneAndEmailConstraint, String> {

    private String email;

    private String phoneNumber;

    @Override
    public void initialize(PhoneAndEmailConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
