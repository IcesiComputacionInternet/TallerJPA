package co.com.icesi.icesiAccountSystem.validation.validators;

import co.com.icesi.icesiAccountSystem.dto.RequestUserDTO;
import co.com.icesi.icesiAccountSystem.validation.interfaces.EmailAndPhoneConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailAndPhoneValidator implements
        ConstraintValidator<EmailAndPhoneConstraint, RequestUserDTO> {
    @Override
    public void initialize(EmailAndPhoneConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(RequestUserDTO dto, ConstraintValidatorContext cxt) {
        return !dto.getPhoneNumber().isBlank() || !dto.getEmail().isBlank();
    }
}
