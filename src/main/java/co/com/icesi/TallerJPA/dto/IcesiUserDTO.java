package co.com.icesi.TallerJPA.dto;

import co.com.icesi.TallerJPA.model.IcesiRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiUserDTO {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;
    private String email;

    private IcesiRoleDTO role;
}
