package co.com.icesi.TallerJPA.dto;

import co.com.icesi.TallerJPA.validations.ValidatePhoneNumber;
import co.com.icesi.TallerJPA.validations.ValidatePhoneNumberAndEmail;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ValidatePhoneNumberAndEmail
public class IcesiUserCreateDTO {
    private String firstName;
    private String lastName;
    private String email;
    @ValidatePhoneNumber
    private String phoneNumber;
    private String password;
    private IcesiRoleCreateDTO role;
}