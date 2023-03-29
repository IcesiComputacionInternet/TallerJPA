package co.com.icesi.tallerjpa.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class TransactionDTO {

    @NotBlank
    private String accountNumberOrigin;
    private String accountNumberDestination;
    @Min(value = 0, message = "The balance must be greater than 0")
    private Long amount;
    private String message;
}
