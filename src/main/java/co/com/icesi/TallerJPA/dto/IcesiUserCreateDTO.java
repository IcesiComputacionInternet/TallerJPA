package co.com.icesi.TallerJPA.dto;

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
    private String role;
}