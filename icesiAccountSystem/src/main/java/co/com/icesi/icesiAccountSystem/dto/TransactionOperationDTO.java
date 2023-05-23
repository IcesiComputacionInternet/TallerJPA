package co.com.icesi.icesiAccountSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionOperationDTO {
    private String result;
    private String accountFrom;
    private String accountTo;
    @Min(value=0, message = "The amount of a transaction must be greater than 0")
    private Long amount;
}
