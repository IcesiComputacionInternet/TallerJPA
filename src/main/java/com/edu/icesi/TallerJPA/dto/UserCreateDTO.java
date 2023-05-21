package com.edu.icesi.TallerJPA.dto;

import com.edu.icesi.TallerJPA.Constraint.ContactNumberConstraint;
import com.edu.icesi.TallerJPA.Constraint.PhoneAndEmailConstraint;
import com.edu.icesi.TallerJPA.model.IcesiRole;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserCreateDTO {

    @NotNull(message = "User needs a name")
    @NotBlank(message = "First name can't be blank")
    private String firstName;

    @NotNull(message = "User needs a lastname")
    @NotBlank(message = "Lastname can't be blank")
    private String lastName;

    @NotNull(message = "User needs a email")
    @NotBlank(message = "Email can't be blank")
    @Email
    private String email;

    @NotNull(message = "User needs a phone number")
    @NotBlank(message = "Phone number can't be blank")
    @ContactNumberConstraint
    private String phoneNumber;

    @NotNull(message = "User needs a password")
    @NotBlank(message = "Password can't be blank")
    private String password;

    @NotNull(message = "User needs a role")
    private IcesiRole icesiRole;

}
