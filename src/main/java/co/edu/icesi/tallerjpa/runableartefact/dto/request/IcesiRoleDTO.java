package co.edu.icesi.tallerjpa.runableartefact.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@Builder
public class IcesiRoleDTO {

    @NotBlank
    private String name;

    private String description;
}
