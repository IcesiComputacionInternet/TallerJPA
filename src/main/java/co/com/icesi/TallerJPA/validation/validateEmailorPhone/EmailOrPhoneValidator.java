package co.com.icesi.TallerJPA.validation.validateEmailorPhone;

import co.com.icesi.TallerJPA.dto.UserCreateDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailOrPhoneValidator implements ConstraintValidator<EmailOrPhoneConstraint, UserCreateDTO> {

    @Override
    public void initialize(EmailOrPhoneConstraint contactNumber) {
    }

    @Override
    public boolean isValid(UserCreateDTO user, ConstraintValidatorContext constraintValidatorContext) {
        return !user.getEmail().isBlank() || !user.getPhoneNumber().isBlank();
    }
}
