package co.com.icesi.TallerJPA.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDTO {

    private String accountNumberFrom;

    private String accountNumberTo;

    @Min(value = 0, message = "The balance must be greater than 0")
    private Long amount;



}
