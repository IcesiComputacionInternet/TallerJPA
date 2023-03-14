package co.edu.icesi.tallerjpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateIcesiUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
}
