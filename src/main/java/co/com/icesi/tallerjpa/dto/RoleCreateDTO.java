package co.com.icesi.tallerjpa.dto;

import co.com.icesi.tallerjpa.model.IcesiUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreateDTO {
    @NotBlank(message = "Description may not be blank")
    private String description;
    @NotNull(message = "Role's name may not be null")
    @NotBlank(message = "Role's name may not be blank")
    private String name;
}
