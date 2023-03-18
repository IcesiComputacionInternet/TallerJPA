package co.com.icesi.icesiAccountSystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private String roleName;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
}
