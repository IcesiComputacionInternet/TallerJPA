package co.com.icesi.tallerjpa.dto;

import co.com.icesi.tallerjpa.model.IcesiAccount;
import co.com.icesi.tallerjpa.model.IcesiRole;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserCreateDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private RoleCreateDTO icesiroleDto;
}
