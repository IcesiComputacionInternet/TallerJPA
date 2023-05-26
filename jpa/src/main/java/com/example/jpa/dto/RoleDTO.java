package com.example.jpa.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class RoleDTO {

    private String description;
    @NotBlank
    private String name;
}
