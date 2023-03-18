package com.example.jpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {

    private String firstName;

    private String lastName;

    private String password;

    private String email;

    private String phoneNumber;

    private RoleDTO role;
}
