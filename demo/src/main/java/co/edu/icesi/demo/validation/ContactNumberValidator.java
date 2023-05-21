package co.edu.icesi.demo.validation;

import javax.validation.ConstraintValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContactNumberValidator implements ConstraintValidator<ContactNumberConstraint, String> {


    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        if(phoneNumber.isBlank()){
            return true;
        }

        return phoneNumber.matches("\\+573[0-9]{9}");
    }
}