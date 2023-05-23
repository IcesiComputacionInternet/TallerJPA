package com.icesi.TallerJPA.validation.emailphonevalue;

import com.icesi.TallerJPA.dto.request.IcesiUserDTO;
import com.icesi.TallerJPA.validation.emailphonevalue.interf.EmailOrPhoneValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailOrPhoneValueValidator implements ConstraintValidator<EmailOrPhoneValue, IcesiUserDTO> {
    @Override
    public void initialize(EmailOrPhoneValue constraintAnnotation) {
    }

    @Override
    public boolean isValid(IcesiUserDTO icesiUserDTO, ConstraintValidatorContext constraintValidatorContext) {
        return !icesiUserDTO.getEmail().isEmpty() || !icesiUserDTO.getPhoneNumber().isEmpty();
    }
}
