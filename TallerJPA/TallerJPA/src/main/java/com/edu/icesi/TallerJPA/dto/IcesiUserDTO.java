package com.edu.icesi.TallerJPA.dto;

import com.edu.icesi.TallerJPA.Constraint.ContactNumberConstraint;
import com.edu.icesi.TallerJPA.Constraint.PhoneAndEmailConstraint;
import com.edu.icesi.TallerJPA.model.IcesiRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiUserDTO {
    private UUID userId;

    private String firstName;

    private String lastName;


    @Email
    private String email;

    @ContactNumberConstraint
    private String phoneNumber;

    private String password;

    private String icesiRole;

}
