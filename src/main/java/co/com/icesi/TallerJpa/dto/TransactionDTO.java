package co.com.icesi.TallerJpa.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@Builder
public class TransactionDTO {
    private String accountNumberOrigin;
    private String accountNumberDestiny;
    @Min(value = 0, message = "El monto de una transacción no puede ser menor a 0.")
    private Long amount;
}
