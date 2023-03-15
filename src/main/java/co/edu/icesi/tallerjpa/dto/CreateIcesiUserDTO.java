package co.edu.icesi.tallerjpa.dto;

import co.edu.icesi.tallerjpa.model.IcesiRole;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class CreateIcesiUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private IcesiRole icesiRole;
}
