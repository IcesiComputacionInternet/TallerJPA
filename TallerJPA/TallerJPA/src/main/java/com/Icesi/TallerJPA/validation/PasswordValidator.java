package com.Icesi.TallerJPA.validation;

import com.Icesi.TallerJPA.enums.ErrorConstants;
import com.Icesi.TallerJPA.error.exception.IcesiError;
import com.Icesi.TallerJPA.error.exception.IcesiException;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<CustomAnnotations.PasswordValidation, String> {
    @Override
    public void initialize(CustomAnnotations.PasswordValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$#%])[A-Za-z\\d@$#%]{8,}$").matcher(str).find();
        if (!valid)
            throw new IcesiException(HttpStatus.BAD_REQUEST, new IcesiError(ErrorConstants.CODE_UD_11, ErrorConstants.CODE_UD_11.getMessage()));
        return true;
    }
}