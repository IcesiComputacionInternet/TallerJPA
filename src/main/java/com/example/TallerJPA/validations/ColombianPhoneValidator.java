package com.example.TallerJPA.validations;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColombianPhoneValidator implements ConstraintValidator<ColombianPhone, String> {
    @Override
    public void initialize(ColombianPhone constraintAnnotation) {

    }
    @Override
    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
        return object.startsWith("+57");
    }

}