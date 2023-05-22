package co.edu.icesi.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleCreateDTO {

    @NotBlank(message = "is missing")
    private String name;
    @NotBlank(message = "is missing")
    private String description;

}
