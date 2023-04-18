package com.example.jpa.validations.implementations;

import com.example.jpa.validations.interfaces.RegionPhoneNumberValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RegionPhoneNumberValidator implements ConstraintValidator<RegionPhoneNumberValidation, String> {

    @Override
    public void initialize(RegionPhoneNumberValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //regex for colombian phone numbers: 2 digits area code, digit "3" and 9 digits phone number
        String regex = "^[+57]{2}3[0-9]{9}$";
        return value != null && value.matches(regex) && (value.length() > 0) && (value.length() < 13);
    }

}
