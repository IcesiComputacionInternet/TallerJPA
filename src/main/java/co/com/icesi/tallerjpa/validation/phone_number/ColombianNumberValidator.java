package co.com.icesi.tallerjpa.validation.phone_number;

import co.com.icesi.tallerjpa.validation.phone_number.interfaces.ColombianNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ColombianNumberValidator implements ConstraintValidator<ColombianNumber, String> {

    @Override
    public void initialize(ColombianNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {return false;}
        if(value.isBlank()){return true;}
        return value.matches("\\+573[0-9]{9}");
    }
}

