package com.example.TallerJPA.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreateDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private String roleName;

}
