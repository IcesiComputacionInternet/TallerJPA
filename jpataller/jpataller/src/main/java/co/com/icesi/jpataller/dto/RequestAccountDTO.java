package co.com.icesi.jpataller.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class RequestAccountDTO {

    @Min(value=0, message = "Menos de 0? Sapo")
    @Max(value=0100)
    private Long balance;

    private TypeAccount type;

    @NotBlank
    private String user;

}
