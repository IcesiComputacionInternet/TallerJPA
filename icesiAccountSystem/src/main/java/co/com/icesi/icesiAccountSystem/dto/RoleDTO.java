package co.com.icesi.icesiAccountSystem.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    @NotNull(message = "The name of a role can't be null")
    @NotBlank(message = "The name of a role can't be blank")
    private String name;
    @NotBlank(message = "The description of a role can't be blank")
    private String description;

}
