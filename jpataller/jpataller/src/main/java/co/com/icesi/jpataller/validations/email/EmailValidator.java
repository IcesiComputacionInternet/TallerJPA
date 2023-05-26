package co.com.icesi.jpataller.validations.email;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<EmailConstraint, String> {
    @Override
    public void initialize(EmailConstraint contactEmail) {

    }

    @Override
    public boolean isValid(String contactEm, ConstraintValidatorContext constraintValidatorContext) {
        return contactEma.contains("@") && contactEm.contains(".");
    }
}
