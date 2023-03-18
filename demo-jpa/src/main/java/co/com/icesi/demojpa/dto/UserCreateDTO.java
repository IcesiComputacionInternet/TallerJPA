package co.com.icesi.demojpa.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserCreateDTO {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private RoleCreateDTO role;
    private List<AccountCreateDTO> accounts;
}
