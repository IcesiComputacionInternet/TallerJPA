package co.com.icesi.TallerJPA.dto;


import co.com.icesi.TallerJPA.validation.annotation.ColombianNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiUserDTO {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

    @ColombianNumber
    private String phoneNumber;
    @NotBlank
    private String password;

    @Email
    private String email;
    private IcesiRoleDTO role;
}
