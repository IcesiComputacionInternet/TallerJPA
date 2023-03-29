package co.edu.icesi.tallerjpa.runableartefact.validations.phoneValidation.implementation;

import co.edu.icesi.tallerjpa.runableartefact.validations.phoneValidation.interfaces.ColombianNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColombianNumberValidator implements ConstraintValidator<ColombianNumber, String> {
    @Override
    public void initialize(ColombianNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {

        String regex = "^\\+573[0-4]\\d{8}$";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
