package co.com.icesi.tallerjpa.validators;

import co.com.icesi.tallerjpa.constraint.EmailorPhoneConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailorPhoneValidator implements ConstraintValidator<EmailorPhoneConstraint, String> {
    @Override
    public void initialize(EmailorPhoneConstraint emailorphone) {
    }

@Override
    public boolean isValid(String contactField, ConstraintValidatorContext cxt) {
        return !contactField.isEmpty();
}
}
