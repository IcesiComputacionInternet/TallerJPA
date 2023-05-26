package co.com.icesi.jpataller.validations.accountNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AccountNumberValidator implements ConstraintValidator<AccountNumberConstraint, String> {
    @Override
    public void initialize(AccountNumberConstraint accountNumber) {

    }

    @Override
    public boolean isValid(String accNum, ConstraintValidatorContext constraintValidatorContext) {
        return accNum != null && accNum.length()==13;
    }
}
