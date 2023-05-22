package co.com.icesi.demojpa.validate.phone;

import co.com.icesi.demojpa.dto.UserCreateDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<PhoneConstraint, UserCreateDTO> {
    @Override
    public void initialize(PhoneConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(UserCreateDTO userCreateDTO, ConstraintValidatorContext constraintValidatorContext) {
        return
                userCreateDTO.getPhone()
                        .replaceAll("\\s+","")
                        .replaceAll("[+]","")
                        .matches("[0-9]{10}")
                        && userCreateDTO.getPhone().contains("+57");
    }
}
