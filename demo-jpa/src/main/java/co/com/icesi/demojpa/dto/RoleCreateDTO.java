package co.com.icesi.demojpa.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RoleCreateDTO {

    private UUID roleId;
    private String name;
    private String description;
}
