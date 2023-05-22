package co.edu.icesi.tallerJPA.dto;

import co.edu.icesi.tallerJPA.enums.AccountType;
import co.edu.icesi.tallerJPA.model.IcesiUser;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AccountDTO {
    private UUID id;
    private String accountNumber;
    private long balance;
    private AccountType type;
    private boolean active;
    private IcesiUser icesiUser;
}
