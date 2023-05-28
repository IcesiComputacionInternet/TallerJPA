package co.com.icesi.TallerJPA.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserPhoneValidator implements ConstraintValidator<ValidatePhoneNumber, String> {
    private static final String PHONE_NUMBER_PATTERN = "^\\+57(1|3|4|5|6|7|8|9)[0-9]{7}$";

    @Override
    public void initialize(ValidatePhoneNumber constraintAnnotation) {
    }
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return true;
        }
        return phoneNumber.matches(PHONE_NUMBER_PATTERN);
    }
}
