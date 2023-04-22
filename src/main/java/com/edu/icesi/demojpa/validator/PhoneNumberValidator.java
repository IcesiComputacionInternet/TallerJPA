package com.edu.icesi.demojpa.validator;

import com.edu.icesi.demojpa.constraint.PhoneNumberConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberConstraint, String> {
    @Override
    public void initialize(PhoneNumberConstraint phoneNumberConstraint){
        ConstraintValidator.super.initialize(phoneNumberConstraint);
    }
    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        boolean verify = false;

        if (verifyDigits(phone) && validateLength(phone)){
            verify = isValidFormat(phone);
        }
        return verify;
    }

    private boolean verifyDigits(String phoneNumber){
        return phoneNumber.matches("[0-9]+");
    }

    private boolean validateLength(String phoneNumber){
        return phoneNumber != null && phoneNumber.length() == 13;
    }

    private boolean isValidFormat(String phoneNumber){
        return phoneNumber.matches("^(/+57 3)/d{9}$");
    }
}
