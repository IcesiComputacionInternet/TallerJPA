package co.com.icesi.TallerJPA.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class TransactionRequestDTO {
    @NotBlank
    private String accountNumberFrom;
    @NotBlank
    private String accountNumberTo;

    @Min(value = 0, message = "The balance must be greater than 0")
    private Long amount;



}
