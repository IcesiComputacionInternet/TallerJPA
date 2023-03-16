package co.edu.icesi.tallerjpa.dto;

import co.edu.icesi.tallerjpa.model.IcesiRole;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class IcesiUserCreateDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    @NotNull(message = "The role of the icesi user can not be null")
    private IcesiRoleCreateDTO icesiRoleCreateDTO;
}
