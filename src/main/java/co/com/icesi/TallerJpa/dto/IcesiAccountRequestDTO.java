package co.com.icesi.TallerJpa.dto;

import co.com.icesi.TallerJpa.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcesiAccountRequestDTO {
    @Min(value = 0, message = "El balance de la cuenta debe ser mayor a 0.")
    private Long balance;

    @NotNull(message = "El tipo de cuenta no puede ser nulo.")
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @NotNull(message = "El email del usuario no puede ser nulo.")
    @NotBlank(message = "El email del usuario no puede ser blanco.")
    @NotEmpty(message = "El email del usuario no puede estar vacio")
    @Email
    private String user;
}
