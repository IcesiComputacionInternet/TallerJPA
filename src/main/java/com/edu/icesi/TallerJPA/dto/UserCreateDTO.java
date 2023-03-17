package com.edu.icesi.TallerJPA.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserCreateDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String password;
}
