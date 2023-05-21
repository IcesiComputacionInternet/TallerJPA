package icesi.university.accountSystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private IcesiRoleDTO role;
}
