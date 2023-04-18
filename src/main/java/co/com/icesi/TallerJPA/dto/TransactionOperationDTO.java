package co.com.icesi.TallerJPA.dto;

import lombok.Builder;
import lombok.Data;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class TransactionOperationDTO {

    @NotNull
    private String accountFrom;
    @NotNull
    private String accountTo;

    @NotBlank
    private Long amount;
    private String result;
}
