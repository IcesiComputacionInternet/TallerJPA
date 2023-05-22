package com.example.jpa.validations.implementations;

import com.example.jpa.dto.UserDTO;
import com.example.jpa.validations.interfaces.AtLeastOneNotNull;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AtLeastOneNotNullValidator implements ConstraintValidator<AtLeastOneNotNull, UserDTO> {

    @Override
    public void initialize(AtLeastOneNotNull constraintAnnotation) {
    }

    @Override
    public boolean isValid(UserDTO value, ConstraintValidatorContext constraintValidatorContext) {
        if(value.getEmail() == null || value.getPhoneNumber() == null) {return false;}
        return !value.getEmail().isBlank() || !value.getPhoneNumber().isBlank();
    }
}
