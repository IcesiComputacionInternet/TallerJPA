package co.com.icesi.demojpa.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class TransactionOperationDTO {

    @NotBlank(message = "Account from is required")
    private String accountFrom;
    @NotBlank(message = "Account to is required")
    private String accountTo;
    @NotBlank(message = "Amount is required")
    @Min(value=1, message = "Amount must be greater than zero")
    private Long amount;

}