package co.com.icesi.tallerjpa.validators;

import co.com.icesi.tallerjpa.constraint.EmailorPhoneConstraint;
import co.com.icesi.tallerjpa.dto.RequestUserDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailorPhoneValidator implements ConstraintValidator<EmailorPhoneConstraint, RequestUserDTO> {
    @Override
    public void initialize(EmailorPhoneConstraint emailorphone) {
    }

@Override
    public boolean isValid(RequestUserDTO user, ConstraintValidatorContext cxt) {
        return !user.getPhoneNumber().isBlank() || !user.getEmail().isBlank();
    }
}
