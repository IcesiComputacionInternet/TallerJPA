package co.com.icesi.TallerJpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IcesiRoleCreateDTO {
    private String description;
    private String name;
}
