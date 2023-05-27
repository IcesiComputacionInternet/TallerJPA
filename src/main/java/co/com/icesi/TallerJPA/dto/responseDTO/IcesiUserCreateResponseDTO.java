package co.com.icesi.TallerJPA.dto.responseDTO;

import co.com.icesi.TallerJPA.dto.requestDTO.IcesiRoleCreateDTO;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiUserCreateResponseDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private IcesiRoleCreateDTO role;
}