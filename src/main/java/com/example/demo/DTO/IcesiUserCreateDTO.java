package com.example.demo.DTO;

import com.example.demo.model.IcesiRole;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IcesiUserCreateDTO {
    
    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String password;

    private IcesiRole icesiRole;
}
