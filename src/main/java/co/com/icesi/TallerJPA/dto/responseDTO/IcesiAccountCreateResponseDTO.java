package co.com.icesi.TallerJPA.dto.responseDTO;

import co.com.icesi.TallerJPA.enums.AccountType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class IcesiAccountCreateResponseDTO {
    private UUID accountId;
    private String accountNumber;
    private long balance;
    private AccountType accountType;
    private boolean active;
    private IcesiUserCreateResponseDTO icesiUser;
}
