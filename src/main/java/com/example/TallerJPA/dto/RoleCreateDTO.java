package com.example.TallerJPA.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class RoleCreateDTO {
    @NotBlank
    private String name;

    private String description;
}
