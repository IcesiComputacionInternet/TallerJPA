package co.com.icesi.TallerJPA.dto;

import lombok.Builder;
import lombok.Data;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class TransactionOperationDTO {


    private String accountFrom;
    private String accountTo;
    @NotBlank
    @Min(value = 0, message = "The amount must be greater than 0")
    private Long amount;
    private String result;
}
