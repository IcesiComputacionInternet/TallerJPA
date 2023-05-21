package com.example.tallerjpa.validation.phonenumber;

import com.example.tallerjpa.validation.phonenumber.interfaces.ColombianNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ColombianNumberValidator implements ConstraintValidator<ColombianNumber, String> {

    @Override
    public void initialize(ColombianNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.matches("\\+573[0-9]{9}");
    }
}
