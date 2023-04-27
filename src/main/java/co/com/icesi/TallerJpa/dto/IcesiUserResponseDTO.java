package co.com.icesi.TallerJpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IcesiUserResponseDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private IcesiRoleDTO roleDTO;
}
