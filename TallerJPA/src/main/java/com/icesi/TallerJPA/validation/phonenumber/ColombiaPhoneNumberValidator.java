package com.icesi.TallerJPA.validation.phonenumber;


import com.icesi.TallerJPA.validation.phonenumber.interf.ColombiaPhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ColombiaPhoneNumberValidator implements ConstraintValidator<ColombiaPhoneNumber, String> {
    @Override
    public void initialize(ColombiaPhoneNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        return phoneNumber.matches("^3[0-9]{9}$");
    }
}