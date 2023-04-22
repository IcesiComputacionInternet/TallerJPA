package co.com.icesi.TallerJPA.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserPhoneAndEmailValidator implements ConstraintValidator<ValidatePhoneNumberAndEmail, String> {
    private static final String PHONE_NUMBER_PATTERN = "^\\+57(1|3|4|5|6|7|8|9)[0-9]{7}$";
    private static final String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    @Override
    public void initialize(ValidatePhoneNumberAndEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        return PHONE_NUMBER_PATTERN.matches(value) || EMAIL_PATTERN.matches(value);
    }
}
