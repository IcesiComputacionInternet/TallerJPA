package co.edu.icesi.demo.validation;

import co.edu.icesi.demo.dto.IcesiUserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailOrPhoneValidator implements ConstraintValidator<EmailOrPhoneConstraint, IcesiUserDto> {
    @Override
    public void initialize(EmailOrPhoneConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(IcesiUserDto icesiUserDto, ConstraintValidatorContext constraintValidatorContext) {
        return !(icesiUserDto.getEmail().isBlank() && icesiUserDto.getPhoneNumber().isBlank());
    }
}
