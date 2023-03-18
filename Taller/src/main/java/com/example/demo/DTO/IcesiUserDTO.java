package com.example.demo.DTO;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Builder
public class IcesiUserDTO {

    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
}
