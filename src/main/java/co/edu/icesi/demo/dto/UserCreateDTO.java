package co.edu.icesi.demo.dto;

import co.edu.icesi.demo.validation.constraint.EmailOrPhoneNumberExistConstraint;
import co.edu.icesi.demo.validation.constraint.PhoneNumberConstraint;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@EmailOrPhoneNumberExistConstraint
public class UserCreateDTO {


    private String firstName;

    private String lastName;

    private String email;

    @PhoneNumberConstraint
    private String phoneNumber;

    private String password;

    private String roleName;
}
