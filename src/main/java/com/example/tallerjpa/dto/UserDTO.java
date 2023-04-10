package com.example.tallerjpa.dto;

import lombok.*;

@Data
@Builder
public class UserDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private String role;
}
