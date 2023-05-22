package com.edu.icesi.demojpa.dto.response;

import com.edu.icesi.demojpa.constraint.PhoneAndEmailConstraint;
import com.edu.icesi.demojpa.constraint.PhoneNumberConstraint;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@PhoneAndEmailConstraint
public class ResponseUserDTO {
    @NotBlank(message = "The field can't be blank")
    @NotNull(message = "The field can't be null")
    private String firstName;
    @NotBlank(message = "The field can't be blank")
    @NotNull(message = "The field can't be null")
    private String lastName;
    private String email;
    @PhoneNumberConstraint
    private String phoneNumber;
    @NotBlank(message = "The field can't be blank")
    @NotNull(message = "The field can't be null")
    private String password;
    @NotBlank(message = "The field can't be blank")
    @NotNull(message = "The field can't be null")
    private String roleType;
}
