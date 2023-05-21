package co.edu.icesi.tallerjpa.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
public class IcesiRoleCreateDTO {
    @NotNull(message = "The description can not be null")
    @NotBlank(message = "The description can not be blank")
    @NotEmpty(message = "The description can not be empty")
    private String description;
    @NotNull(message = "The name of the role can not be null")
    @NotBlank(message = "The name of the role can not be blank")
    @NotEmpty(message = "The name of the role can not be empty")
    private String name;
}
