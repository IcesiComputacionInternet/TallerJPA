package co.edu.icesi.tallerjpa.runableartefact.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class IcesiRoleDTO {

    private String name;
    private String description;
}
