package co.com.icesi.tallerjpa.validation.email_or_phone;

import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.validation.email_or_phone.interfaces.EmailOrPhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailOrPhoneNumberValidator implements ConstraintValidator<EmailOrPhoneNumber, RequestUserDTO> {
    @Override
    public void initialize(EmailOrPhoneNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(RequestUserDTO value, ConstraintValidatorContext constraintValidatorContext) {
        return !value.getEmail().isBlank() || !value.getPhoneNumber().isBlank();
    }
}
