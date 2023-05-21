package co.com.icesi.TallerJpa.dto;

import co.com.icesi.TallerJpa.enums.AccountType;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class IcesiAccountRequestDTO {
    @Min(value = 0, message = "El balance de la cuenta debe ser mayor a 0.")
    private Long balance;
    @NotNull(message = "El tipo de cuenta no puede ser nulo.")
    @Enumerated(EnumType.STRING)
    private AccountType type;
    @NotBlank(message = "El usuario no puede ser vacio.")
    private String user;
}
