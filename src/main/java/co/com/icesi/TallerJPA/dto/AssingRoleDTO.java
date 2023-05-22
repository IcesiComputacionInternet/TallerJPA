package co.com.icesi.TallerJPA.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class AssingRoleDTO {

    @NotBlank
    private String emailOfUser;

    @NotBlank
    private String roleToAssing;
}
