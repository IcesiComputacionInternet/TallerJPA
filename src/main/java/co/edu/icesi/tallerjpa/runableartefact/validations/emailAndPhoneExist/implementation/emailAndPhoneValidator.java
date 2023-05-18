package co.edu.icesi.tallerjpa.runableartefact.validations.emailAndPhoneExist.implementation;

import co.edu.icesi.tallerjpa.runableartefact.validations.emailAndPhoneExist.interfaces.emailAndPhone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class emailAndPhoneValidator implements ConstraintValidator<emailAndPhone, String> {
    @Override
    public void initialize(emailAndPhone constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.isBlank();
    }
}
