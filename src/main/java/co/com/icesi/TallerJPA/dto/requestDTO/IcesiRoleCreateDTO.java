package co.com.icesi.TallerJPA.dto.requestDTO;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiRoleCreateDTO {
    private String name;
    private String description;
}
