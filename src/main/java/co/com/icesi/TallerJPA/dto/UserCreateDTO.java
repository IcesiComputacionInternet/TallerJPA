package co.com.icesi.TallerJPA.dto;

import co.com.icesi.TallerJPA.model.IcesiRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class UserCreateDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;

    private IcesiRole role;
}
