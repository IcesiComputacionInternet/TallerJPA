package com.edu.icesi.demojpa.dto.request;

import com.edu.icesi.demojpa.constraint.PhoneAndEmailConstraint;
import com.edu.icesi.demojpa.constraint.PhoneNumberConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestUserDTO {
    private String firstName;
    private String lastName;
    @PhoneAndEmailConstraint
    private String email;
    @PhoneNumberConstraint
    private String phoneNumber;
    private String password;
    private String roleType;
}
