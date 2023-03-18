package co.edu.icesi.tallerJPA.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IcesiUserCreateDTO {
    private String firstName;

    private String lastname;

    private String email;

    private String phone;

    private String password;

    private IcesiRoleCreateDTO icesiRoleCreateDTO;
}
