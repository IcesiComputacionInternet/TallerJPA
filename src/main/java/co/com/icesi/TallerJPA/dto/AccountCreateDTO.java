package co.com.icesi.TallerJPA.dto;

import co.com.icesi.TallerJPA.Enum.AccountType;
import co.com.icesi.TallerJPA.model.IcesiUser;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class AccountCreateDTO {

    @Min(value = 0, message = "The balance must be greater than 0")
    private long balance;

    @NotBlank
    private AccountType type;

    @NotBlank
    private String user;
}
