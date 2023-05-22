package co.com.icesi.tallerjpa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
public record RoleDTO(
        @NotBlank
        String name,
        String description
) {}
