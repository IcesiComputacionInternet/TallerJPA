package co.com.icesi.tallerjpa.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {

    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
}
