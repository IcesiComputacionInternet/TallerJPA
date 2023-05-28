package co.edu.icesi.demo.validation.validator;

import co.edu.icesi.demo.dto.TransactionDTO;
import co.edu.icesi.demo.validation.constraint.TransactionAccountConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TransactionAccountValidator implements ConstraintValidator<TransactionAccountConstraint, TransactionDTO> {
    @Override
    public void initialize(TransactionAccountConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(TransactionDTO transactionDTO, ConstraintValidatorContext constraintValidatorContext) {
        return transactionDTO.getAccountNumberFrom()!=null || transactionDTO.getAccountNumberTo()!=null;
    }
}
