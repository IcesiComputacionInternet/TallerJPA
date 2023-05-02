package co.com.icesi.tallerjpa.dto;

import co.com.icesi.tallerjpa.Enum.AccountType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
public class RequestAccountDTO {
    @Min(value = 0, message = "")
    private Long balance;
    private AccountType type;
    private String user;
}