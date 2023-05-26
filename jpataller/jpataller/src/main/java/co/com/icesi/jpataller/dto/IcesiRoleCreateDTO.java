package co.com.icesi.jpataller.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcesiRoleCreateDTO {
    private String description;

    private String name;
}
