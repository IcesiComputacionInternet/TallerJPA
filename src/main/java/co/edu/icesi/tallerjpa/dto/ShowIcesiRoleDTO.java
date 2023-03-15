package co.edu.icesi.tallerjpa.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ShowIcesiRoleDTO {
    private UUID roleId;
    private String description;
    private String name;
    private List<ShowIcesiUserDTO> icesiUsers;
}
