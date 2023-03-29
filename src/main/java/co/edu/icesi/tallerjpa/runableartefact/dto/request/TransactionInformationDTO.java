package co.edu.icesi.tallerjpa.runableartefact.dto.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.jackson.JsonComponent;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@JsonComponent
public class TransactionInformationDTO {

    @NotBlank
    private String accountNumberOrigin;
    @NotBlank
    private String accountNumberDestination;
    @Min(value = Long.MIN_VALUE, message = "The amount must be greater than 0")
    @Max(value = Long.MAX_VALUE - 1, message = "The amount must be less than " + Long.MAX_VALUE)
    private Long amount;
    @NotBlank
    private String typeOfTransaction;
}
