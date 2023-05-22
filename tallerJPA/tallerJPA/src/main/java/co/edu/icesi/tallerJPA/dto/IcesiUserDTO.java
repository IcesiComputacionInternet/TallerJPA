package co.edu.icesi.tallerJPA.dto;

import co.edu.icesi.tallerJPA.model.Role;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class IcesiUserDTO {

    private UUID userId;

    private String firstName;

    private String lastname;

    private String email;

    private String phoneNumber;

    private String password;

    private Role role;
}
