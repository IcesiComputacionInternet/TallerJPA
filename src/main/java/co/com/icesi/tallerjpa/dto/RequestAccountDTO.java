package co.com.icesi.tallerjpa.dto;

import co.com.icesi.tallerjpa.Enum.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestAccountDTO {
    @Min(value = 0, message = "Account balance can't be below 0")
    private Long balance;
    private AccountType type;
    private String user;
}