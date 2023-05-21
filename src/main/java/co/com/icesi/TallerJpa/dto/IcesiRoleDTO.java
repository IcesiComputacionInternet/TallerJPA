package co.com.icesi.TallerJpa.dto;

import lombok.Builder;

import javax.validation.constraints.NotBlank;

@Builder
public record IcesiRoleDTO(
        @NotBlank
        String name,
        String description) {
}
