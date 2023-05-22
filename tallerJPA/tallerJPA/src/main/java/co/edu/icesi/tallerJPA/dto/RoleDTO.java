package co.edu.icesi.tallerJPA.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RoleDTO {
    private UUID roleId;
    private String name;
    private String description;
}

