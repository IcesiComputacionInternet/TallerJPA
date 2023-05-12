package co.com.icesi.tallerjpa.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Builder
public record RoleDTO(
        @NotBlank
        String name,
        String description
) {}
