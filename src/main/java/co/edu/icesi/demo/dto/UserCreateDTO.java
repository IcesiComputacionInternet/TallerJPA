package co.edu.icesi.demo.dto;

import co.edu.icesi.demo.validation.constraint.EmailOrPhoneNumberExistConstraint;
import co.edu.icesi.demo.validation.constraint.PhoneNumberConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EmailOrPhoneNumberExistConstraint
public class UserCreateDTO {


    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;


    private String email;

    @PhoneNumberConstraint
    private String phoneNumber;

    @NotBlank
    private String password;

    @NotBlank
    private String roleName;
}
