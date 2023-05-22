package co.edu.icesi.demo.validation.validator;

import co.edu.icesi.demo.dto.UserDTO;
import co.edu.icesi.demo.validation.constraint.EmailOrPhoneNumberExistConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailOrPhoneNumberExistValidator implements ConstraintValidator<EmailOrPhoneNumberExistConstraint, UserDTO> {

    @Override
    public void initialize(EmailOrPhoneNumberExistConstraint constraintAnnotation) {
       ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserDTO userDTO, ConstraintValidatorContext constraintValidatorContext) {
      return !userDTO.getEmail().isEmpty()  || !userDTO.getPhoneNumber().isEmpty();
    }

}
