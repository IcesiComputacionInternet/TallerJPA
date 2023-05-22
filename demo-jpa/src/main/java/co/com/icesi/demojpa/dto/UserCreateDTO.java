package co.com.icesi.demojpa.dto;


import co.com.icesi.demojpa.validation.PhoneNumberConstraint;
import co.com.icesi.demojpa.validation.ValidEmail;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserCreateDTO {

    private UUID userId;
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @ValidEmail
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    @PhoneNumberConstraint
    private String phoneNumber;
    @NotNull(message = "Role is required")
    private RoleCreateDTO role;
    private List<AccountCreateDTO> accounts;
}
