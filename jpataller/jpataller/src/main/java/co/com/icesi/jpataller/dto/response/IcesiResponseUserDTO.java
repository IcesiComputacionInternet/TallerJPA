package co.com.icesi.jpataller.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IcesiResponseUserDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private IcesiResponseRoleDTO role;
}
