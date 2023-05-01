package co.edu.icesi.demo.validation;

import javax.validation.ConstraintValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContactNumberValidator implements ConstraintValidator<ContactNumberConstraint, String> {
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        return phoneNumber != null && phoneNumber.matches( "^(\\+57)?\\s?3\\d{9}$")
                && (phoneNumber.length() >=10) && (phoneNumber.length() < 15);
    }
}