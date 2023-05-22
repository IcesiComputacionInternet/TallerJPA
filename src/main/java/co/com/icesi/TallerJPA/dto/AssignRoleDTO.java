package co.com.icesi.TallerJPA.dto;

import lombok.Builder;

import javax.validation.constraints.NotBlank;


@Builder
public record AssignRoleDTO(

        @NotBlank
        String username,
        @NotBlank
        String roleName
) {
}
