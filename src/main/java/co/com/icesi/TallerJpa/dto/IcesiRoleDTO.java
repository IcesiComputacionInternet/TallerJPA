package co.com.icesi.TallerJpa.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class IcesiRoleDTO {
    @NotNull
    @NotBlank
    @NotEmpty
    private String description;
    @NotNull
    @NotBlank
    @NotEmpty
    private String name;
}
