package co.com.icesi.tallerjpa.validators;

import co.com.icesi.tallerjpa.constraint.CellphoneConstraint;
import co.com.icesi.tallerjpa.dto.RequestUserDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserNumberValidator implements ConstraintValidator<CellphoneConstraint, RequestUserDTO> {
    @Override
    public void initialize(CellphoneConstraint contactNumber) {
    }

    @Override
    public boolean isValid(RequestUserDTO user, ConstraintValidatorContext cxt) {
        return !user.getPhoneNumber().isBlank() && user.getPhoneNumber().matches("^\\+57(3[0-9]{9})")
                && (user.getPhoneNumber().length() == 13);
    }
}
