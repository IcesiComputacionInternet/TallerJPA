package co.edu.icesi.tallerjpa.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class IcesiRoleShowForUserDTO {
    private String description;
    private String name;
}
