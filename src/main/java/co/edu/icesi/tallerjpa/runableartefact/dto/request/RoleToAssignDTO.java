package co.edu.icesi.tallerjpa.runableartefact.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleToAssignDTO {

        @NotBlank
        @NotNull
        private UUID userId;

        @NotBlank
        @NotNull
        private String roleName;
}
