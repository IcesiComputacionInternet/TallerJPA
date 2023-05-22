package co.edu.icesi.demo.dto;

import co.edu.icesi.demo.validation.constraint.CustomEmailConstraint;
import co.edu.icesi.demo.validation.constraint.EmailOrPhoneNumberExistConstraint;
import co.edu.icesi.demo.validation.constraint.PhoneNumberConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EmailOrPhoneNumberExistConstraint
public class UserCreateDTO {


    @NotBlank(message = "is missing")
    private String firstName;

    @NotBlank(message = "is missing")
    private String lastName;


    @CustomEmailConstraint
    private String email;

    @PhoneNumberConstraint
    private String phoneNumber;

    @NotBlank(message = "is missing")
    private String password;

    @NotBlank(message = "is missing")
    private String roleName;
}
