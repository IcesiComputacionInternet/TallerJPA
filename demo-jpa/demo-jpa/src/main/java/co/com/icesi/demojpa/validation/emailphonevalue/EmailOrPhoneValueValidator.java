package co.com.icesi.demojpa.validation.emailphonevalue;



import co.com.icesi.demojpa.dto.request.UserCreateDTO;
import co.com.icesi.demojpa.validation.emailphonevalue.interf.EmailOrPhoneValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailOrPhoneValueValidator implements ConstraintValidator<EmailOrPhoneValue, UserCreateDTO> {
    @Override
    public void initialize(EmailOrPhoneValue constraintAnnotation) {
    }

    @Override
    public boolean isValid(UserCreateDTO icesiUserDTO, ConstraintValidatorContext constraintValidatorContext) {
        return !icesiUserDTO.getEmail().isEmpty() || !icesiUserDTO.getPhone().isEmpty();
    }
}
