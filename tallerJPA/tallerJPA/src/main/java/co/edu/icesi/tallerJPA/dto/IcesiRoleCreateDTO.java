package co.edu.icesi.tallerJPA.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IcesiRoleCreateDTO {
    private String name;
    private String description;
}

