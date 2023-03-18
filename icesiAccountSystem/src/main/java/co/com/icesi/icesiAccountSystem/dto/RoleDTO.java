package co.com.icesi.icesiAccountSystem.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class RoleDTO {

    private String description;
    private String name;
}
