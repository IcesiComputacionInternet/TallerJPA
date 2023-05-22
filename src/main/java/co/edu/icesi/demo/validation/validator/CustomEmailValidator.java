package co.edu.icesi.demo.validation.validator;

import co.edu.icesi.demo.validation.constraint.CustomEmailConstraint;
import co.edu.icesi.demo.validation.constraint.PhoneNumberConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomEmailValidator implements ConstraintValidator<CustomEmailConstraint,String> {


    @Override
    public void initialize(CustomEmailConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return email == null || email.isEmpty() || email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}") ;
    }
}
