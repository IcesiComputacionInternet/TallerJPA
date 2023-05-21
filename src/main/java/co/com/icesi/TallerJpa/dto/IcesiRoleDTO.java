package co.com.icesi.TallerJpa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiRoleDTO {
    @NotNull
    @NotBlank
    @NotEmpty
    private String name;
    private String description;
}
