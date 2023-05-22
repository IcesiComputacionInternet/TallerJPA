package com.edu.icesi.demojpa.dto.request;

import com.edu.icesi.demojpa.constraint.PhoneAndEmailConstraint;
import com.edu.icesi.demojpa.constraint.PhoneNumberConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@PhoneAndEmailConstraint
public class RequestUserDTO {
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
