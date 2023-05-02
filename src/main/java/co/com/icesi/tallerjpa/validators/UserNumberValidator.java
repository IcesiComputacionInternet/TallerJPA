package co.com.icesi.tallerjpa.validators;

import co.com.icesi.tallerjpa.constraint.CellphoneConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserNumberValidator implements ConstraintValidator<CellphoneConstraint, String> {
    @Override
    public void initialize(CellphoneConstraint contactNumber) {
    }

    @Override
    public boolean isValid(String contactField, ConstraintValidatorContext cxt) {
        return contactField != null && contactField.matches("\\+57[0-9]")
                && (contactField.length() == 10);
    }
}
