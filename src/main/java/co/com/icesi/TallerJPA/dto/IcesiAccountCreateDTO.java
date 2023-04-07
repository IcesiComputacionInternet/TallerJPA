package co.com.icesi.TallerJPA.dto;

import co.com.icesi.TallerJPA.enums.AccountType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IcesiAccountCreateDTO {
    private Long balance;
    private AccountType accountType;
    private String icesiUser;
}