package com.edu.icesi.demojpa.validator;

import com.edu.icesi.demojpa.constraint.PhoneAndEmailConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneAndNumberValidator implements ConstraintValidator<PhoneAndEmailConstraint, String> {
    @Override
    public void initialize(PhoneAndEmailConstraint phoneAndEmailConstraint){
        ConstraintValidator.super.initialize(phoneAndEmailConstraint);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
