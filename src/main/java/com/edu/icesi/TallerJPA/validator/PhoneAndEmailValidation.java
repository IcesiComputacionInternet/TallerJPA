package com.edu.icesi.TallerJPA.validator;

import com.edu.icesi.TallerJPA.Constraint.PhoneAndEmailConstraint;
import com.edu.icesi.TallerJPA.dto.UserCreateDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneAndEmailValidation implements ConstraintValidator<PhoneAndEmailConstraint, UserCreateDTO> {

    @Override
    public void initialize(PhoneAndEmailConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserCreateDTO user, ConstraintValidatorContext constraintValidatorContext) {
        return user.getEmail() != null && user.getPhoneNumber() != null;
    }
}
