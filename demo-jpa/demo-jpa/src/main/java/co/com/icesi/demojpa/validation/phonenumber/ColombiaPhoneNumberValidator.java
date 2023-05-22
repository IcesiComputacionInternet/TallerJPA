package co.com.icesi.demojpa.validation.phonenumber;


import co.com.icesi.demojpa.validation.phonenumber.interf.ColombiaPhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ColombiaPhoneNumberValidator implements ConstraintValidator<ColombiaPhoneNumber, String> {
    @Override
    public void initialize(ColombiaPhoneNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        return phoneNumber.matches("\\+573[0-9]{9}");
    }
}