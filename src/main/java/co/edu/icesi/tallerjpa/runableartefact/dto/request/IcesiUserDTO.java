package co.edu.icesi.tallerjpa.runableartefact.dto.request;

import co.edu.icesi.tallerjpa.runableartefact.validations.emailAndPhoneExist.interfaces.emailAndPhone;
import co.edu.icesi.tallerjpa.runableartefact.validations.phoneValidation.interfaces.ColombianNumber;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiUserDTO {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email(message = "Email should be valid", regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    @emailAndPhone
    private String email;

    @ColombianNumber
    @emailAndPhone
    private String phoneNumber;

    @NotBlank
    private String password;

    @NotEmpty
    private String roleName;
    
}
