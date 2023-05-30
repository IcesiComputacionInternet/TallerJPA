package co.edu.icesi.demo.dto;

import co.edu.icesi.demo.model.IcesiAccount;
import co.edu.icesi.demo.model.IcesiRole;
import co.edu.icesi.demo.validation.ContactNumberConstraint;
import co.edu.icesi.demo.validation.EmailOrPhoneConstraint;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;


import javax.persistence.Entity;
import java.util.List;
import java.util.UUID;


@Builder
@Data
@EmailOrPhoneConstraint
public class IcesiUserDto {

    private UUID userId;

    @NonNull
    private String firstname;

    @NonNull
    private String lastName;

    private String email;

    @ContactNumberConstraint
    private String phoneNumber;

    private String password;

    private IcesiRoleDto role;

}
