package icesi.university.accountSystem.validators;

import icesi.university.accountSystem.interfaces.PhoneNumberConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements
        ConstraintValidator<PhoneNumberConstraint, String> {
    @Override
    public void initialize(PhoneNumberConstraint contactNumber) {
    }

    @Override
    public boolean isValid(String contactField,
                           ConstraintValidatorContext cxt) {
        return contactField != null && contactField.matches("^(\\+57\\s)?3\\d{9}$")
                && (contactField.length() >=10) && (contactField.length() < 13);
    }

}