package co.com.icesi.tallerjpa.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RoleDTO {
    private UUID roleId;
    private String name;
    private String description;
}
