package co.com.icesi.icesiAccountSystem.dto;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class RoleDTO {
    @NotBlank(message = "The description of a role can't be blank")
    private String description;
    @NotBlank(message = "The name of a role can't be blank")
    @NotNull(message = "The name of a role can't be null")
    private String name;
}
