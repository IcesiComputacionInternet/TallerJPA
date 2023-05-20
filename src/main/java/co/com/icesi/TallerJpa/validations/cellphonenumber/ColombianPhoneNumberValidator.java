package co.com.icesi.TallerJpa.validations.cellphonenumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ColombianPhoneNumberValidator implements ConstraintValidator<ColombianPhoneNumber, String> {
    private static final String COLOMBIAN_NUMBER_REGEX = "^(\\+57|57|\\+57\s|57\s)?[1-9][0-9]{9}$";

    @Override
    public void initialize(ColombianPhoneNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s.isBlank()){
            return true;
        }
        return s.matches(COLOMBIAN_NUMBER_REGEX);
    }
}
