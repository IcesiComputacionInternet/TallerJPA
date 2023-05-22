package co.com.icesi.TallerJPA.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;


public record AssignRoleDTO(

        @NotBlank
        String username,
        @NotBlank
        String roleName
) {
}
