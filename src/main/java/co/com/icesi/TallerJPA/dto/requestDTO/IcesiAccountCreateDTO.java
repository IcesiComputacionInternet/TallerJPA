package co.com.icesi.TallerJPA.dto.requestDTO;

import co.com.icesi.TallerJPA.enums.AccountType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IcesiAccountCreateDTO {
    private Long balance;
    private AccountType accountType;
    private IcesiUserCreateDTO icesiUser;
}