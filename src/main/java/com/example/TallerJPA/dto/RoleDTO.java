package com.example.TallerJPA.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class RoleDTO {
    @NotBlank
    private String name;

    private String description;
}
