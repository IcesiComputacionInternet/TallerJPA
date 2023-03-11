package co.com.icesi.tallerjpa.dto;

import co.com.icesi.tallerjpa.enums.TypeAccount;
import co.com.icesi.tallerjpa.model.IcesiUser;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AccountDTO {
    private UUID accountId;
    private String accountNumber;
    private Long balance;
    private TypeAccount type;
    private boolean active;

    private IcesiUser user;
}
