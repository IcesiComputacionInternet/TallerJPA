package co.edu.icesi.tallerjpa.dto;

import co.edu.icesi.tallerjpa.model.IcesiRole;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class IcesiUserCreateDTO {
    @NotNull(message = "The first name can not be null")
    @NotBlank(message = "The first name can not be blank")
    @NotEmpty(message = "The first name can not be empty")
    private String firstName;

    @NotNull(message = "The last name can not be null")
    @NotBlank(message = "The last name can not be blank")
    @NotEmpty(message = "The last name can not be empty")
    private String lastName;

    @NotNull(message = "The email can not be null")
    @NotBlank(message = "The email can not be blank")
    @NotEmpty(message = "The email can not be empty")
    @Email(message = "The email must be a well-formed email address")
    private String email;

    @NotNull(message = "The phone number can not be null")
    @NotBlank(message = "The phone number can not be blank")
    @NotEmpty(message = "The phone number can not be empty")
    private String phoneNumber;

    @NotNull(message = "The password can not be null")
    @NotBlank(message = "The password can not be blank")
    @NotEmpty(message = "The password can not be empty")
    private String password;
    @NotNull(message = "The role of the icesi user can not be null")
    @Valid
    private IcesiRoleCreateDTO icesiRoleCreateDTO;
}
