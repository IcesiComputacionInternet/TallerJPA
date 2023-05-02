package co.com.icesi.tallerjpa.dto;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class RequestUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String role;
}