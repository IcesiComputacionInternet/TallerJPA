package com.example.demo.DTO;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiUserDTO {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private UUID icesiRoleId;
}
