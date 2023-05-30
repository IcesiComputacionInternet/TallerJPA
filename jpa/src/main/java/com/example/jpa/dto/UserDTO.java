package com.example.jpa.dto;

import com.example.jpa.validations.interfaces.AtLeastOneNotNull;
import com.example.jpa.validations.interfaces.RegionPhoneNumberValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Email
@AtLeastOneNotNull
@AllArgsConstructor
public class UserDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String password;

    @Email
    @NotNull
    private String email;

    @NotNull
    @RegionPhoneNumberValidation
    private String phoneNumber;

    private RoleDTO role;
}
