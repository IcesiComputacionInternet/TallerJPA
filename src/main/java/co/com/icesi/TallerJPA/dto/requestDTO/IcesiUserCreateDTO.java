package co.com.icesi.TallerJPA.dto.requestDTO;

import co.com.icesi.TallerJPA.validations.ValidatePhoneNumber;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiUserCreateDTO {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    @Email
    private String email;
    @NotNull
    @ValidatePhoneNumber
    private String phoneNumber;
    private String password;
    @NotNull
    private IcesiRoleCreateDTO role;
}