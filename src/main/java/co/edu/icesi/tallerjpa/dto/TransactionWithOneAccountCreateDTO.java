package co.edu.icesi.tallerjpa.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class TransactionWithOneAccountCreateDTO {
    @NotNull(message = "The account number can not be null")
    @Pattern(regexp = "[0-9]{3}-[0-9]{6}-[0-9]{2}", message = "Invalid format of account number")
    private String accountNumber;

    @Min(value = 0, message = "The min value for the balance is 0")
    private long amount;
}
