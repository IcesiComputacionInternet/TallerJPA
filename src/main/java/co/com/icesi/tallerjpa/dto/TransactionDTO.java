package co.com.icesi.tallerjpa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    private String accountFrom;
    private String accountTo;
    @Min(value = 0, message = "The amount must be greater than 0")
    private Long amount;
    private String result;
}
