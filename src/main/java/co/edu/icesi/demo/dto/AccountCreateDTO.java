package co.edu.icesi.demo.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class AccountCreateDTO {

    private String accountNumber;

    @Min(value=0, message = "min value is 0")
    @Max(value=100)
    private long balance;

    private String type;

    private boolean active;

    @NotBlank
    private String userEmail;
}