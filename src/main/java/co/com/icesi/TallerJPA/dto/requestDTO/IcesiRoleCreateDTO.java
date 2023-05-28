package co.com.icesi.TallerJPA.dto.requestDTO;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiRoleCreateDTO {
    @NotBlank
    private String name;
    private String description;
}
