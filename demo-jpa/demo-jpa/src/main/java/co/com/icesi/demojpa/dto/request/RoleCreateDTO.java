package co.com.icesi.demojpa.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreateDTO {

    @NotEmpty
    private String description;
    @NotEmpty
    private String name;
}
