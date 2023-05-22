package co.com.icesi.TallerJPA.dto.requestDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IcesiRoleCreateDTO {
    private String name;
    private String description;
}
