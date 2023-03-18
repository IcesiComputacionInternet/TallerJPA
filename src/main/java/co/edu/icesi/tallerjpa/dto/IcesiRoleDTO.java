package co.edu.icesi.tallerjpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IcesiRoleDTO {

    private String name;
    private String description;

}
