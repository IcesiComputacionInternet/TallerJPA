package icesi.university.accountSystem.dto;

import icesi.university.accountSystem.interfaces.PhoneNumberConstraint;
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
public class RequestUserDTO {

    @NotBlank
    @NotNull
    private String firstName;
    @NotBlank
    @NotNull
    private String lastName;
    @Email
    private String email;
    @NotBlank
    @NotNull
    private String password;
    @PhoneNumberConstraint
    private String phoneNumber;
    @NotNull
    @NotBlank
    private String role;
}
