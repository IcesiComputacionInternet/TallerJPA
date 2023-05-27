package co.com.icesi.TallerJPA.dto.requestDTO;

import co.com.icesi.TallerJPA.validations.ValidatePhoneNumber;
import co.com.icesi.TallerJPA.validations.ValidatePhoneNumberAndEmail;
import lombok.*;

@Getter
@Setter
@Builder
@ValidatePhoneNumberAndEmail
@NoArgsConstructor
@AllArgsConstructor
public class IcesiUserCreateDTO {
    private String firstName;
    private String lastName;
    //@ValidatePhoneNumberAndEmail
    private String email;
    //@ValidatePhoneNumberAndEmail
    private String phoneNumber;
    private String password;
    private IcesiRoleCreateDTO role;
}