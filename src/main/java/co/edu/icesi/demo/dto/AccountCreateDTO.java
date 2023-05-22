package co.edu.icesi.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreateDTO {

    private String accountNumber;

    @Min(value=0, message = "min value is 0")
    @Max(value=1000000000000L, message = "max value is 1000000000000")
    private long balance;

    @NotBlank(message = "is missing")
    private String type;

    private boolean active;

    @NotBlank(message = "is missing")
    private String userEmail;
}