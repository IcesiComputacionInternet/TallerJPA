package co.com.icesi.TallerJpa.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class IcesiRoleDTO {
    @NotBlank
    private String description;
    @NotBlank
    private String name;
}
