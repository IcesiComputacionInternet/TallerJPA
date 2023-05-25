package co.com.icesi.TallerJpa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    @NotNull(message = "can't be null.")
    @NotBlank(message = "can't be blank.")
    @NotEmpty(message = "can't be empty.")
    private String accountNumberOrigin;
    private String accountNumberDestiny;
    @Min(value = 0, message = "El monto de una transaccion no puede ser menor a 0.")
    private Long amount;
    private String message;
}
