package com.example.tallerjpa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private String role;
}
