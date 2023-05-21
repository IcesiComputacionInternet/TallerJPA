package com.edu.icesi.demojpa.validator;

import com.edu.icesi.demojpa.constraint.PhoneAndEmailConstraint;
import com.edu.icesi.demojpa.dto.request.RequestUserDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneAndNumberValidator implements ConstraintValidator<PhoneAndEmailConstraint, RequestUserDTO> {
    @Override
    public void initialize(PhoneAndEmailConstraint phoneAndEmailConstraint){
        ConstraintValidator.super.initialize(phoneAndEmailConstraint);
    }

    @Override
    public boolean isValid(RequestUserDTO requestUserDTO, ConstraintValidatorContext constraintValidatorContext) {
        return !requestUserDTO.getEmail().isEmpty() || !requestUserDTO.getPhoneNumber().isEmpty();
    }
}
