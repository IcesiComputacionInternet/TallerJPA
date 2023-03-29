package co.edu.icesi.tallerjpa.runableartefact.dto.request;

import co.edu.icesi.tallerjpa.runableartefact.validations.phoneValidation.interfaces.ColombianNumber;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class IcesiUserDTO {
    @NotBlank
    private String firstName;

    private String lastName;

    @Email(message = "Email should be valid", regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    private String email;

    @ColombianNumber
    private String phoneNumber;

    @NotBlank
    private String password;

    @NotEmpty
    private String roleName;
}
