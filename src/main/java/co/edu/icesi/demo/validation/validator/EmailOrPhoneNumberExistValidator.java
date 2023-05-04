package co.edu.icesi.demo.validation.validator;

import co.edu.icesi.demo.dto.UserCreateDTO;
import co.edu.icesi.demo.validation.constraint.EmailOrPhoneNumberExistConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailOrPhoneNumberExistValidator implements ConstraintValidator<EmailOrPhoneNumberExistConstraint, UserCreateDTO> {

    @Override
    public void initialize(EmailOrPhoneNumberExistConstraint constraintAnnotation) {
       ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserCreateDTO userCreateDTO, ConstraintValidatorContext constraintValidatorContext) {
      return !userCreateDTO.getEmail().isEmpty()  || !userCreateDTO.getPhoneNumber().isEmpty();
    }

}
