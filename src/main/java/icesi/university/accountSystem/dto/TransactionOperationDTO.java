package icesi.university.accountSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@Builder
@AllArgsConstructor
public class TransactionOperationDTO {

    private String accountFrom;
    private String accountTo;
    @Min(value = 0, message = "The amount don't be negative")
    private Long amount;

}
