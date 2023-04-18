package com.example.jpa.validations.implementations;

import com.example.jpa.validations.interfaces.AtLeastOneNotNull;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AtLeastOneNotNullValidator implements ConstraintValidator<AtLeastOneNotNull, String> {

    private String[] fields;

    @Override
    public void initialize(AtLeastOneNotNull constraintAnnotation) {
        this.fields = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean atLeastOneNotNull = false;
        for (String field : fields) {
            if (field.isEmpty()) {
                atLeastOneNotNull = true;
                break;
            }
        }
        return atLeastOneNotNull;
    }
}
