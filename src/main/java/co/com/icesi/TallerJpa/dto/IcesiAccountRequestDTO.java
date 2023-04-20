package co.com.icesi.TallerJpa.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class IcesiAccountRequestDTO {
    @Min(value = 0, message = "El balance de la cuenta debe ser mayor a 0.")
    private long balance;
    @NotNull
    private String type;
    @NotBlank
    private String user;
}
