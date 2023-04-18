package com.example.jpa.dto;

import com.example.jpa.validations.interfaces.AtLeastOneNotNull;
import com.example.jpa.validations.interfaces.RegionPhoneNumberValidation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {

    private String firstName;

    private String lastName;

    private String password;

    @AtLeastOneNotNull(fields = {"email", "phoneNumber"})
    private String email;

    @AtLeastOneNotNull(fields = {"email", "phoneNumber"})
    @RegionPhoneNumberValidation
    private String phoneNumber;

    private RoleDTO role;
}
