package com.edu.icesi.demojpa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleCreateDTO {
    private String description;
    private String name;
}
