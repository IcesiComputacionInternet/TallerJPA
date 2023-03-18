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
    //Referenciando el role con IcesiRoleCreateDTO, se asegura que los datos se mantengan consistentes y normalizados.
    private IcesiRoleCreateDTO role;
}
