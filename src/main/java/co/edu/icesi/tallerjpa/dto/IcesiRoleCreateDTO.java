package co.edu.icesi.tallerjpa.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
public class IcesiRoleCreateDTO {
    private String description;
    @NotNull(message = "The name of the role can not be null")
    private String name;
}
