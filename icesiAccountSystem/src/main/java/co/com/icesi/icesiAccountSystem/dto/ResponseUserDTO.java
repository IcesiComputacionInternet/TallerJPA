package co.com.icesi.icesiAccountSystem.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseUserDTO {
    private RoleDTO role;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
}
