package com.example.demo.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseIcesiUserDTO {
    
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private ResponseIcesiRoleDTO icesiRoleCreateDTO;
}
