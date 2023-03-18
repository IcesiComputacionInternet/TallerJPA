package icesi.university.accountSystem.dto;

import icesi.university.accountSystem.model.IcesiRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiUserDTO {
    private UUID userId;
    private String firstName;
    private String lastName;

    private String email;

    private  String phoneNumber;

    private String password;

    private IcesiRole role;
}
