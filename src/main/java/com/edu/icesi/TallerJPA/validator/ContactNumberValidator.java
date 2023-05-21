package com.edu.icesi.TallerJPA.validator;


import com.edu.icesi.TallerJPA.Constraint.ContactNumberConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

//https://www.baeldung.com/spring-mvc-custom-validator

public class ContactNumberValidator implements ConstraintValidator<ContactNumberConstraint, String>{

    @Override
    public void initialize(ContactNumberConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        boolean verify = false;

        if (verifyDigits(phone) && validateLength(phone)){
            verify = verifyFormatPhoneNumber(phone);
        }

        return verify;
    }

    private boolean validateLength(String phoneNumber){
        return phoneNumber != null && phoneNumber.length() == 13;
    }

    private boolean verifyDigits(String phoneNumber){
        return phoneNumber.matches("[0-9]+");
    }

    private boolean verifyFormatPhoneNumber(String phoneNumber){
        return phoneNumber.matches("^(/+57 3)/d{9}$");
    }
}
