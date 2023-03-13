package co.com.icesi.TallerJPA.dto.response;

import co.com.icesi.TallerJPA.dto.RoleCreateDTO;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserResponseDTO {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;

    private RoleCreateDTO role;
}
