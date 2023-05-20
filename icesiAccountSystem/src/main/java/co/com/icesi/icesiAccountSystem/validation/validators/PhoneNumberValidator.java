package co.com.icesi.icesiAccountSystem.validation.validators;

import co.com.icesi.icesiAccountSystem.dto.RequestUserDTO;
import co.com.icesi.icesiAccountSystem.validation.interfaces.PhoneNumberConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements
        ConstraintValidator<PhoneNumberConstraint, RequestUserDTO> {

    @Override
    public void initialize(PhoneNumberConstraint contactNumber) {
    }

    @Override
    public boolean isValid(RequestUserDTO dto, ConstraintValidatorContext cxt) {
        return !dto.getPhoneNumber().isBlank() && dto.getPhoneNumber().matches("[+]573[0-5][0-9]{8}")
                && (dto.getPhoneNumber().length() == 13);
    }

}
