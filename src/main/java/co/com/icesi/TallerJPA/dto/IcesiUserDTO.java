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

    private String firstName;

    private String lastName;

    @ColombianNumber
    private String phoneNumber;

    @NotBlank
    private String password;

    @NotBlank
    @Email
    private String email;
    private IcesiRoleDTO role;
}
