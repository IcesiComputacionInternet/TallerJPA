package co.com.icesi.demojpa.api;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AccountNumberValidator implements ConstraintValidator<AccountNumber,String> {

    @Override
    public void initialize(AccountNumber accountNumber){

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s!=null && s.length()==13;
    }
}
