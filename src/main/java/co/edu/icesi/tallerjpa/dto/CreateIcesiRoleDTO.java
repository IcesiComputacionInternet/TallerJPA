package co.edu.icesi.tallerjpa.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CreateIcesiRoleDTO {
    private UUID roleId;
    private String description;
    private String name;
}
