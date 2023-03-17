package com.edu.icesi.TallerJPA.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleCreateDTO {

    private String description;

    private String name;
}
