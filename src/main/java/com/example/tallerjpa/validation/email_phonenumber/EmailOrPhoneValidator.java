package com.example.tallerjpa.validation.email_phonenumber;

import com.example.tallerjpa.dto.UserDTO;
import com.example.tallerjpa.validation.email_phonenumber.interfaces.EmailOrPhone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailOrPhoneValidator implements ConstraintValidator<EmailOrPhone, UserDTO> {
    @Override
    public void initialize(EmailOrPhone constraintAnnotation) {
    }

    @Override
    public boolean isValid(UserDTO value, ConstraintValidatorContext constraintValidatorContext) {
        return !value.getEmail().isBlank() || !value.getPhoneNumber().isBlank();
    }
}