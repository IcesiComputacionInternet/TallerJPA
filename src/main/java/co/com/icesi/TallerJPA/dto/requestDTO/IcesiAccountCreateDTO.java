package co.com.icesi.TallerJPA.dto.requestDTO;

import co.com.icesi.TallerJPA.enums.AccountType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class IcesiAccountCreateDTO {
    private Long balance;
    @NotBlank
    private AccountType accountType;
    @NotBlank
    private IcesiUserCreateDTO icesiUser;
}