package co.edu.icesi.tallerjpa.runableartefact.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiRoleDTO {

    @NotBlank
    private String name;

    private String description;
}
