package co.com.icesi.TallerJpa.dto;

import co.com.icesi.TallerJpa.validations.cellphonenumber.ColombianPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiUserRequestDTO {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    private String email;
    @ColombianPhoneNumber
    private String phoneNumber;
    @NotBlank
    private String password;
    @NotNull
    private String role;
}
