package co.com.icesi.TallerJPA.validation.validatePhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ColombianNumberValidator implements ConstraintValidator<ColombianNumberConstraint, String> {

    @Override
    public void initialize(ColombianNumberConstraint contactNumber) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s.isBlank()){
            return true;
        }
        return s.matches("\\+573[0-9]{9}");
    }
}

