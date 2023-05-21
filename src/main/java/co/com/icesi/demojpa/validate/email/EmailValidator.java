package co.com.icesi.demojpa.validate.email;

import co.com.icesi.demojpa.dto.UserCreateDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<EmailConstraint, UserCreateDTO> {

    @Override
    public void initialize(EmailConstraint contactNumber) {
    }
    @Override
    public boolean isValid(UserCreateDTO userCreateDTO, ConstraintValidatorContext constraintValidatorContext) {
        return userCreateDTO.getEmail().contains("@") && userCreateDTO.getEmail().contains(".");
    }
}
