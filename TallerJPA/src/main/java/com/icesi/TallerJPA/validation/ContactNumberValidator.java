package com.icesi.TallerJPA.validation;

import com.icesi.TallerJPA.validation.notation.ValidatePhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContactNumberValidator implements ConstraintValidator<ValidatePhoneNumber, String> {

    @Override
    public void initialize(ValidatePhoneNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String reg = "^(\\+57|0057|57)?[1-9][0-9]{7}$";
        System.out.println("Hola");
        System.out.println(reg);
        return s.matches(reg);
    }
}
